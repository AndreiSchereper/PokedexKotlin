package nl.schereper.andrei.pokedex.models

/**
 * Payload returned by
 * `https://pokeapi.co/api/v2/pokemon?limit=…&offset=…`
 */
data class PokemonListResponse(
    val count: Int,                // total Pokémon on the server
    val next: String?,             // URL of the next page (null = last)
    val previous: String?,         // URL of the previous page (null = first)
    val results: List<PokemonEntry>
)

/**
 * Lightweight entry inside [PokemonListResponse.results].
 *
 * Only a name and the URL pointing to the full Pokémon resource.
 */
data class PokemonEntry(
    val name: String,
    val url: String
)

/**
 * UI-friendly model after we enrich a [PokemonEntry]
 * with an `id`, first `type`, and a sprite URL.
 */
data class PokemonListItemData(
    val name: String,
    val id: Int,
    val imageUrl: String,
    val type: String
)