package nl.schereper.andrei.pokedex.models

import com.google.gson.annotations.SerializedName

/* ───────── full Pokémon payload ───────── */
data class PokemonDetails(
    val id: Int,
    val name: String,
    @SerializedName("base_experience") val base_experience: Int,
    val height: Int,          // decimetres
    val weight: Int,          // hectograms
    val types: List<PokemonTypeSlot>,
    val abilities: List<AbilitySlot>,
    val stats: List<PokemonStat>,
    val sprites: Sprites
)

/* slots */
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

/* helper */
data class NamedApiResource(
    val name: String,
    val url: String
)

/* only path we need: sprites.other["official-artwork"].front_default */
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

/* pokemon-species/{id} */
data class PokemonSpecies(
    @SerializedName("evolution_chain") val evolution_chain: EvolutionChainRef
) { data class EvolutionChainRef(val url: String) }

/* evolution-chain/{id} (simplified) */
data class EvolutionChain(
    val id: Int,
    val chain: EvolutionNode
)

data class EvolutionNode(
    val species: NamedApiResource,
    @SerializedName("evolves_to") val evolves_to: List<EvolutionNode>
)

/* tiny extension to reach the art URL safely */
val PokemonDetails.imageUrl: String?
    get() = sprites.other?.officialArtwork?.frontDefault