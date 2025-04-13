package nl.schereper.andrei.pokedex.utils

fun extractPokemonId(url: String): Int {
    return url.trimEnd('/').split("/").last().toInt()
}

fun getPokemonImageUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"