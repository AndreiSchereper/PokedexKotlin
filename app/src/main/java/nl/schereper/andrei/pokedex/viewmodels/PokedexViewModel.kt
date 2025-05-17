package nl.schereper.andrei.pokedex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.models.PokemonEntry
import nl.schereper.andrei.pokedex.models.PokemonListItemData
import nl.schereper.andrei.pokedex.network.ApiClient
import nl.schereper.andrei.pokedex.utils.extractPokemonId
import nl.schereper.andrei.pokedex.utils.getPokemonImageUrl
import kotlin.math.min       // ← needed for page-clamp

class PokedexViewModel : ViewModel() {

    /* ─────── tuning knobs ─────── */
    private val pageSize  = 20
    private val TOTAL_CAP = 1_000        // <── the 1000-Pokémon hard limit

    /* ─────── paging state ─────── */
    private val _pagedList  = MutableStateFlow<List<PokemonListItemData>>(emptyList())
    val pagedList: StateFlow<List<PokemonListItemData>> = _pagedList

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _endReached = MutableStateFlow(false)
    val endReached: StateFlow<Boolean> = _endReached

    private var currentPage = 0   // 0-based index of the next page to fetch

    /* ─────── catalogue for search ─────── */
    private val _allEntries = MutableStateFlow<List<PokemonEntry>>(emptyList())
    private var allNamesJob: Job? = null

    /* ─────── search state ─────── */
    private val _searchQuery   = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<PokemonListItemData>>(emptyList())

    /** Stream exposed to the UI. */
    val visiblePokemon: StateFlow<List<PokemonListItemData>> =
        combine(_searchQuery, _pagedList, _searchResults) { query, paged, search ->
            if (query.isBlank()) paged else search
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /* ───────── initial load ───────── */
    init {
        loadPagedBlock()        // first 20 for the grid
        ensureAllNamesLoaded()  // lightweight list of the first 1 000 names
    }

    /* ───────── public API ───────── */
    fun onSearchQueryChange(newValue: String) {
        _searchQuery.value = newValue
        if (newValue.isBlank()) {
            _searchResults.value = emptyList()
        } else {
            performSearch(newValue)
        }
    }

    fun maybeLoadNextPage() {
        if (_searchQuery.value.isBlank()) loadPagedBlock()
    }

    /* ───────── internal: endless scroll ───────── */
    private fun loadPagedBlock() {
        if (_isLoading.value || _endReached.value) return

        val offset = currentPage * pageSize
        if (offset >= TOTAL_CAP) {        // ← stop at 1 000
            _endReached.value = true
            return
        }

        val limitForThisCall = min(pageSize, TOTAL_CAP - offset)

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.apiService
                    .getPokemonList(limit = limitForThisCall, offset = offset)

                if (response.results.isEmpty()) {
                    _endReached.value = true
                }

                val newItems = response.results
                    .map { entry -> async { toDetailedItem(entry) } }
                    .mapNotNull { it.await() }

                currentPage++
                _pagedList.value = _pagedList.value + newItems
            } finally {
                _isLoading.value = false
            }
        }
    }

    /* ───────── internal: load ALL names (1 000 max) ───────── */
    private fun ensureAllNamesLoaded() {
        if (_allEntries.value.isNotEmpty() || allNamesJob != null) return

        allNamesJob = viewModelScope.launch {
            try {
                val response = ApiClient.apiService
                    .getPokemonList(limit = TOTAL_CAP, offset = 0)
                _allEntries.value = response.results
            } finally {
                allNamesJob = null
            }
        }
    }

    /* ───────── internal: search by name ───────── */
    private var searchJob: Job? = null
    private fun performSearch(rawQuery: String) {
        ensureAllNamesLoaded()

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val q = rawQuery.trim().lowercase()
            val matches = _allEntries.value
                .filter { it.name.contains(q) }
                .take(60)                       // practical UI cap

            val detailed = matches
                .map { entry -> async { toDetailedItem(entry) } }
                .mapNotNull { it.await() }

            _searchResults.value = detailed
        }
    }

    /* ───────── helper: build UI item ───────── */
    private suspend fun toDetailedItem(entry: PokemonEntry): PokemonListItemData? =
        try {
            val id      = extractPokemonId(entry.url)
            val details = ApiClient.apiService.getPokemonDetails(id)
            val type    = details.types.firstOrNull()?.type?.name ?: "normal"
            PokemonListItemData(
                name     = entry.name,
                id       = id,
                imageUrl = getPokemonImageUrl(id),
                type     = type
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    override fun onCleared() {
        viewModelScope.launch {
            allNamesJob?.cancelAndJoin()
            searchJob?.cancelAndJoin()
        }
        super.onCleared()
    }
}