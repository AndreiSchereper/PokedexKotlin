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

/**
 * “Stats” tab – shows every base-stat as a coloured progress bar.
 */
@Composable
fun StatsTab(
    pokemon : PokemonDetails,
    barColor: Color                 // accent colour from first type
) {
    val maxStat = 255f              // game-design hard-cap

    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        pokemon.stats.forEach { stat ->

            /* normalise value to [0;1] for progress bar */
            val value = stat.base_stat.coerceAtMost(maxStat.toInt())
            val ratio = value / maxStat
            val name  = stat.stat.name
                .replace('-', ' ')
                .replaceFirstChar(Char::uppercase)

            /* label row:  stat name | numeric value */
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text(value.toString(), fontWeight = FontWeight.SemiBold)
            }

            /* coloured bar */
            LinearProgressIndicator(
                progress   = ratio,
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color      = barColor
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}