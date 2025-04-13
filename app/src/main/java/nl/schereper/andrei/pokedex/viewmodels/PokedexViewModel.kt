package nl.schereper.andrei.pokedex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.models.PokemonListItemData
import nl.schereper.andrei.pokedex.network.ApiClient
import nl.schereper.andrei.pokedex.utils.extractPokemonId
import nl.schereper.andrei.pokedex.utils.getPokemonImageUrl

class PokedexViewModel : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonListItemData>>(emptyList())
    val pokemonList: StateFlow<List<PokemonListItemData>> = _pokemonList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _endReached = MutableStateFlow(false)
    val endReached: StateFlow<Boolean> = _endReached

    private var currentPage = 0
    private val pageSize = 20

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        if (_isLoading.value || _endReached.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val offset = currentPage * pageSize
                val response = ApiClient.apiService.getPokemonList(limit = pageSize, offset = offset)

                if (response.results.isEmpty() || response.next == null) {
                    _endReached.value = true
                }

                val newItems = response.results.map { entry ->
                    async {
                        try {
                            val id = extractPokemonId(entry.url)
                            val details = ApiClient.apiService.getPokemonDetails(id)
                            val type = details.types.firstOrNull()?.type?.name ?: "normal"
                            PokemonListItemData(
                                name = entry.name,
                                id = id,
                                imageUrl = getPokemonImageUrl(id),
                                type = type
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    }
                }.mapNotNull { it.await() }

                currentPage++
                _pokemonList.value = _pokemonList.value + newItems
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}