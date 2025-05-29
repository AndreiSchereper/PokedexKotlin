package nl.schereper.andrei.pokedex.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.models.*
import nl.schereper.andrei.pokedex.network.ApiClient
import nl.schereper.andrei.pokedex.utils.extractPokemonId
import nl.schereper.andrei.pokedex.utils.getPokemonImageUrl

/** Simple data-holder for one evolution step */
data class EvolutionStage(
    val name: String,
    val id: Int,
    val imageUrl: String
)

class PokemonDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["id"])

    private val _pokemon = MutableStateFlow<PokemonDetails?>(null)
    val pokemon: StateFlow<PokemonDetails?> = _pokemon

    private val _evolution = MutableStateFlow<List<EvolutionStage>>(emptyList())
    val evolution: StateFlow<List<EvolutionStage>> = _evolution

    init { load() }

    private fun load() = viewModelScope.launch {
        /* 1 – details                */
        val details = ApiClient.apiService.getPokemonDetails(pokemonId)
        _pokemon.value = details

        /* 2 – species (for chain url) */
        val species   = ApiClient.apiService.getPokemonSpecies(pokemonId)
        val evoUrl    = species.evolution_chain.url
        val chainId   = extractPokemonId(evoUrl)

        /* 3 – evolution-chain         */
        val chain     = ApiClient.apiService.getEvolutionChain(chainId)
        _evolution.value = flattenChain(chain.chain)
            .map { res ->
                val id = extractPokemonId(res.url)
                EvolutionStage(
                    name      = res.name,
                    id        = id,
                    imageUrl  = getPokemonImageUrl(id)
                )
            }
    }

    /** Pick the *first* branch at every split, returning a linear list. */
    private fun flattenChain(node: EvolutionNode): List<NamedApiResource> {
        val list = mutableListOf<NamedApiResource>()
        var current: EvolutionNode? = node
        while (current != null) {
            list += current.species
            current = current.evolves_to.firstOrNull()
        }
        return list
    }
}