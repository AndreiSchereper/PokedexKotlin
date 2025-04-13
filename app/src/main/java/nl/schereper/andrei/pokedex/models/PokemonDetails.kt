package nl.schereper.andrei.pokedex.models

data class PokemonDetails(
    val types: List<PokemonTypeSlot>
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: TypeData
)

data class TypeData(
    val name: String,
    val url: String
)