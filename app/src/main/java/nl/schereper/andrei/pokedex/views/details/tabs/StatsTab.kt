package nl.schereper.andrei.pokedex.views.details.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.schereper.andrei.pokedex.models.PokemonDetails

@Composable
fun StatsTab(pokemon: PokemonDetails, barColor: Color) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        pokemon.stats.forEach { stat ->
            val value = stat.base_stat.coerceAtMost(255)
            val ratio = value / 255f
            val name  = stat.stat.name.replace('-', ' ').replaceFirstChar(Char::uppercase)

            Row(
                Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text(value.toString(), fontWeight = FontWeight.SemiBold)
            }

            LinearProgressIndicator(
                progress   = ratio,
                modifier   = Modifier.fillMaxWidth().height(8.dp).clip(MaterialTheme.shapes.small),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color      = barColor
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}