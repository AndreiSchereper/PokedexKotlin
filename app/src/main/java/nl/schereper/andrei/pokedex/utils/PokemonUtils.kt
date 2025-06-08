package nl.schereper.andrei.pokedex.utils

/**
 * Extracts the numeric Pokémon ID from a resource URL, e.g.
 * `https://pokeapi.co/api/v2/pokemon/25/` → **25**.
 *
 * Returns `-1` if the URL does not end with a number.
 */
fun extractPokemonId(url: String): Int =
    url.trimEnd('/')
        .substringAfterLast('/')
        .toIntOrNull() ?: -1   // safe fallback

/* ------------------------------------------------------------------ */

private const val SPRITE_BASE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

/**
 * Builds the public Pokémon sprite URL for the provided [id].
 *
 * Example: `getPokemonImageUrl(25)` →
 * `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png`
 */
fun getPokemonImageUrl(id: Int): String = "$SPRITE_BASE/$id.png"