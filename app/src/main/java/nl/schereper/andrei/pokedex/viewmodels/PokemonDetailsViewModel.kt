package nl.schereper.andrei.pokedex.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.models.PokemonDetails
import nl.schereper.andrei.pokedex.network.ApiClient

class PokemonDetailsViewModel(
    savedStateHandle: SavedStateHandle     // nav-args arrive here
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["id"])

    private val _pokemon = MutableStateFlow<PokemonDetails?>(null)
    val pokemon: StateFlow<PokemonDetails?> = _pokemon

    init { load() }

    private fun load() = viewModelScope.launch {
        runCatching {
            ApiClient.apiService.getPokemonDetails(pokemonId)
        }.onSuccess { _pokemon.value = it }
    }
}