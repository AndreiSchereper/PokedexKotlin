package nl.schereper.andrei.pokedex.views.details.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.schereper.andrei.pokedex.models.PokemonDetails

/**
 * “About” tab shown inside the details screen.
 *
 * Displays a simple two-column table with key–value pairs:
 *  • Name / ID / Base XP / Weight / Height
 *  • concatenated Types & Abilities
 *
 * Layout deliberately keeps a fixed 8 dp vertical rhythm so every line
 * has the same spacing, matching the mock-ups you used earlier.
 */
@Composable
fun AboutTab(pokemon: PokemonDetails) {

    /* -------- dataset -------- */
    val rows = listOf(
        "Name"      to pokemon.name.replaceFirstChar(Char::uppercase),
        "ID"        to pokemon.id.toString().padStart(3, '0'),
        "Base XP"   to "${pokemon.base_experience} XP",
        "Weight"    to "%.1f kg".format(pokemon.weight / 10f),
        "Height"    to "%.1f m".format(pokemon.height / 10f),
        "Types"     to pokemon.types.joinToString { it.type.name.replaceFirstChar(Char::uppercase) },
        "Abilities" to pokemon.abilities.joinToString { it.ability.name.replaceFirstChar(Char::uppercase) }
    )

    /* -------- UI -------- */
    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        rows.forEach { (label, value) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                /* left column: label */
                Text(
                    text       = label,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.weight(1f)
                )

                /* right column: value */
                Text(
                    text  = value,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}