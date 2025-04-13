package nl.schereper.andrei.pokedex.utils

fun extractPokemonId(url: String): Int {
    return url.trimEnd('/').split("/").last().toInt()
}