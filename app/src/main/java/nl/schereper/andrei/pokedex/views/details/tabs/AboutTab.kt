package nl.schereper.andrei.pokedex.views.details.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.schereper.andrei.pokedex.models.PokemonDetails

@Composable
fun AboutTab(pokemon: PokemonDetails) {
    val rows = listOf(
        "Name"      to pokemon.name.replaceFirstChar { it.uppercase() },
        "ID"        to pokemon.id.toString().padStart(3, '0'),
        "Base Exp." to "${pokemon.base_experience} XP",
        "Weight"    to "%.1f kg".format(pokemon.weight / 10f),
        "Height"    to "%.1f m".format(pokemon.height / 10f),
        "Types"     to pokemon.types.joinToString { it.type.name.replaceFirstChar(Char::uppercase) },
        "Abilities" to pokemon.abilities.joinToString { it.ability.name.replaceFirstChar(Char::uppercase) }
    )

    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        rows.forEach { (label, value) ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}