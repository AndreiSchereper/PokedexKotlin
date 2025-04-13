package nl.schereper.andrei.pokedex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.models.PokemonEntry
import nl.schereper.andrei.pokedex.network.ApiClient

class PokedexViewModel : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<PokemonEntry>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntry>> = _pokemonList

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

                currentPage++
                _pokemonList.value = _pokemonList.value + response.results
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}