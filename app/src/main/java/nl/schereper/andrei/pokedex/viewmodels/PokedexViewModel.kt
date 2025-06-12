package nl.schereper.andrei.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import nl.schereper.andrei.pokedex.models.PokemonEntry
import nl.schereper.andrei.pokedex.models.PokemonListItemData
import nl.schereper.andrei.pokedex.network.ApiClient
import nl.schereper.andrei.pokedex.utils.extractPokemonId
import nl.schereper.andrei.pokedex.utils.getPokemonImageUrl
import kotlin.math.min

class PokedexViewModel : ViewModel() {

    /* ───── tuning knobs ───── */
    private val pageSize  = 20
    private val TOTAL_CAP = 1_000          // stop at #1000

    /* ───── paging state ───── */
    private val _pagedList = MutableStateFlow<List<PokemonListItemData>>(emptyList())
    val pagedList: StateFlow<List<PokemonListItemData>> = _pagedList

    private val _isLoading  = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _endReached = MutableStateFlow(false)
    val endReached: StateFlow<Boolean> = _endReached

    private var currentPage = 0

    /* ───── catalogue for search ───── */
    private val _allEntries = MutableStateFlow<List<PokemonEntry>>(emptyList())
    private var allNamesJob: Job? = null

    /* ───── search state ───── */
    private val _searchQuery   = MutableStateFlow("")
    val  searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<PokemonListItemData>>(emptyList())

    /** Stream exposed to UI – either paged list or search results. */
    val visiblePokemon: StateFlow<List<PokemonListItemData>> =
        combine(_searchQuery, _pagedList, _searchResults) { q, paged, search ->
            if (q.isBlank()) paged else search
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /* ───────────────────── init ───────────────────── */
    init {
        loadPagedBlock()        // first page for grid
        ensureAllNamesLoaded()  // names for search
    }

    /* ───────────────── UI entry points ───────────────── */

    /** Called every time the Search TextField changes. */
    fun onSearchQueryChange(newValue: String) {
        _searchQuery.value = newValue
        if (newValue.isBlank()) _searchResults.value = emptyList()
        else                    performSearch(newValue)
    }

    /** Grid tells us which index is currently the last visible item. */
    fun onLastVisible(index: Int) {
        if (_searchQuery.value.isNotBlank()) return
        if (_isLoading.value || _endReached.value) return
        if (index == _pagedList.value.lastIndex) loadPagedBlock()
    }

    /* ────────────── public helper for Favourites ────────────── */

    /**
     * Ensures a specific Pokémon (by id) is present in [_pagedList].
     * *Called from Favourites screen so every favourite card is guaranteed
     * to have data, even if the user never scrolled that far in the Pokédex tab.*
     */
    fun ensurePokemonLoaded(id: Int) {
        if (_pagedList.value.any { it.id == id }) return           // already cached

        viewModelScope.launch {
            runCatching {
                val details = ApiClient.apiService.getPokemonDetails(id)
                val type    = details.types.firstOrNull()?.type?.name ?: "normal"

                _pagedList.update { current ->
                    current + PokemonListItemData(
                        name     = details.name,
                        id       = id,
                        imageUrl = getPokemonImageUrl(id),
                        type     = type
                    )
                }
            }
        }
    }

    /* ─────────── sprite pre-fetch for splash ─────────── */
    private var didPrefetch = false

    suspend fun prefetchSpritesBlocking(context: Context) {
        if (didPrefetch || _pagedList.value.isEmpty()) return
        didPrefetch = true

        withContext(Dispatchers.IO) {
            val loader = context.imageLoader
            _pagedList.value.forEach { item ->
                loader.execute(
                    ImageRequest.Builder(context)
                        .data(item.imageUrl)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build()
                )   // blocks
            }
        }
    }

    /* ─────────── internal: endless scroll pager ─────────── */
    private fun loadPagedBlock() {
        if (_isLoading.value || _endReached.value) return

        val offset = currentPage * pageSize
        if (offset >= TOTAL_CAP) { _endReached.value = true; return }

        val limit = min(pageSize, TOTAL_CAP - offset)

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resp = ApiClient.apiService.getPokemonList(limit, offset)

                if (resp.results.isEmpty()) _endReached.value = true

                val newItems = resp.results
                    .map { entry -> async { toDetailedItem(entry) } }
                    .mapNotNull { it.await() }

                currentPage++
                _pagedList.value += newItems
            } finally { _isLoading.value = false }
        }
    }

    /* ─────────── internal: load all names for search ─────────── */
    private fun ensureAllNamesLoaded() {
        if (_allEntries.value.isNotEmpty() || allNamesJob != null) return

        allNamesJob = viewModelScope.launch {
            try {
                val resp = ApiClient.apiService.getPokemonList(TOTAL_CAP, 0)
                _allEntries.value = resp.results
            } finally { allNamesJob = null }
        }
    }

    /* ─────────── internal: search by name ─────────── */
    private var searchJob: Job? = null
    private fun performSearch(raw: String) {
        ensureAllNamesLoaded()

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val q = raw.trim().lowercase()
            val matches = _allEntries.value.filter { it.name.contains(q) }.take(60)

            val detailed = matches
                .map { entry -> async { toDetailedItem(entry) } }
                .mapNotNull { it.await() }

            _searchResults.value = detailed
        }
    }

    /* convert PokémonEntry → PokémonListItemData */
    private suspend fun toDetailedItem(entry: PokemonEntry): PokemonListItemData? = try {
        val id   = extractPokemonId(entry.url)
        val det  = ApiClient.apiService.getPokemonDetails(id)
        val type = det.types.firstOrNull()?.type?.name ?: "normal"
        PokemonListItemData(entry.name, id, getPokemonImageUrl(id), type)
    } catch (e: Exception) {
        e.printStackTrace(); null
    }

    /* cleanup */
    override fun onCleared() {
        viewModelScope.launch {
            allNamesJob?.cancelAndJoin()
            searchJob?.cancelAndJoin()
        }
        super.onCleared()
    }
}