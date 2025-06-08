package nl.schereper.andrei.pokedex.utils

import androidx.compose.ui.graphics.Color
import nl.schereper.andrei.pokedex.ui.theme.*

/**
 * Maps a Pokémon **type** (lower-case) to the brand colour used
 * throughout the UI.
 *
 * Add or tweak colours in `ui/theme/Type*.kt`, then update this map if
 * you support new generations later on.
 */
val typeColorMap: Map<String, Color> = mapOf(
    "normal"   to TypeNormal,
    "fire"     to TypeFire,
    "water"    to TypeWater,
    "electric" to TypeElectric,
    "grass"    to TypeGrass,
    "ice"      to TypeIce,
    "fighting" to TypeFighting,
    "poison"   to TypePoison,
    "ground"   to TypeGround,
    "flying"   to TypeFlying,
    "psychic"  to TypePsychic,
    "bug"      to TypeBug,
    "rock"     to TypeRock,
    "ghost"    to TypeGhost,
    "dragon"   to TypeDragon,
    "dark"     to TypeDark,
    "steel"    to TypeSteel,
    "fairy"    to TypeFairy
)