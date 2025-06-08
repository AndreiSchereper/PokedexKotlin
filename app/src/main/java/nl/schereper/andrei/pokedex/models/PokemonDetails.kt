package nl.schereper.andrei.pokedex.models

import com.google.gson.annotations.SerializedName

/* ──────────────────────────────────────────
   Full payload of GET /pokemon/{id}
   (only the fields our app actually uses)
   ────────────────────────────────────────── */
data class PokemonDetails(
    val id: Int,
    val name: String,

    /** Raw value from API (XP rewarded when caught). */
    @SerializedName("base_experience") val base_experience: Int,

    /** Height in decimetres (API spec). */
    val height: Int,

    /** Weight in hectograms (API spec). */
    val weight: Int,

    val types:     List<PokemonTypeSlot>,
    val abilities: List<AbilitySlot>,
    val stats:     List<PokemonStat>,

    /** Nested object that contains the official artwork URL. */
    val sprites: Sprites
)

/* ───── small nested DTOs ───── */

data class AbilitySlot(
    val ability: NamedApiResource,
    @SerializedName("is_hidden") val is_hidden: Boolean
)

data class PokemonStat(
    @SerializedName("base_stat") val base_stat: Int,
    val stat: NamedApiResource
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: NamedApiResource
)

data class NamedApiResource(
    val name: String,
    val url: String
)

/* sprites.other["official-artwork"].front_default */
data class Sprites(
    val other: OtherSprites?
) {
    data class OtherSprites(
        @SerializedName("official-artwork")
        val officialArtwork: Artwork?
    ) {
        data class Artwork(
            @SerializedName("front_default")
            val frontDefault: String?
        )
    }
}

/* ───── species & evolution endpoints (simplified) ───── */

data class PokemonSpecies(
    @SerializedName("evolution_chain") val evolution_chain: EvolutionChainRef
) { data class EvolutionChainRef(val url: String) }

data class EvolutionChain(
    val id: Int,
    val chain: EvolutionNode
)

data class EvolutionNode(
    val species: NamedApiResource,
    @SerializedName("evolves_to") val evolves_to: List<EvolutionNode>
)

/* ───── convenient extension for Coil ───── */

/** Shorthand to reach the official artwork URL without deep null-checks. */
val PokemonDetails.imageUrl: String?
    get() = sprites.other?.officialArtwork?.frontDefault