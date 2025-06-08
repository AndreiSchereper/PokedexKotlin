package nl.schereper.andrei.pokedex.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.models.*
import nl.schereper.andrei.pokedex.network.ApiClient
import nl.schereper.andrei.pokedex.utils.extractPokemonId
import nl.schereper.andrei.pokedex.utils.getPokemonImageUrl

/* One row shown in the Evolution tab */
data class EvolutionStage(val name: String, val id: Int, val imageUrl: String)

/**
 * VM behind the Pokémon-details screen.
 *
 * Sequence:
 *      details → species → evolution-chain
 */
class PokemonDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["id"])

    private val _pokemon   = MutableStateFlow<PokemonDetails?>(null)
    val  pokemon : StateFlow<PokemonDetails?>       = _pokemon

    private val _evolution = MutableStateFlow<List<EvolutionStage>>(emptyList())
    val  evolution: StateFlow<List<EvolutionStage>> = _evolution

    init { loadCascade() }

    private fun loadCascade() = viewModelScope.launch {
        /* details */
        val details = ApiClient.apiService.getPokemonDetails(pokemonId)
        _pokemon.value = details

        /* species → chain-id */
        val species = ApiClient.apiService.getPokemonSpecies(pokemonId)
        val chainId = extractPokemonId(species.evolution_chain.url)

        /* evolution chain */
        val chain = ApiClient.apiService.getEvolutionChain(chainId)
        _evolution.value = flattenChain(chain.chain).map { res ->
            val id = extractPokemonId(res.url)
            EvolutionStage(res.name, id, getPokemonImageUrl(id))
        }
    }

    /** Keep the first branch only → simple vertical list */
    private fun flattenChain(node: EvolutionNode): List<NamedApiResource> {
        val out = mutableListOf<NamedApiResource>()
        var cur: EvolutionNode? = node
        while (cur != null) {
            out += cur.species
            cur = cur.evolves_to.firstOrNull()
        }
        return out
    }
}