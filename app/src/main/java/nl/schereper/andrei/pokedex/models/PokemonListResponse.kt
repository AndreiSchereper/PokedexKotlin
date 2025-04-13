package nl.schereper.andrei.pokedex.models

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonEntry>
)

data class PokemonEntry(
    val name: String,
    val url: String
)