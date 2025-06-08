package nl.schereper.andrei.pokedex.views.details.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.schereper.andrei.pokedex.viewmodels.EvolutionStage
import nl.schereper.andrei.pokedex.views.details.components.EvolutionCard

/**
 * Evolution tab: shows a vertical list of [EvolutionCard]s
 * with a chevron between each stage.
 *
 *  @param stages      linear path returned by the VM
 *  @param borderType  type-name whose colour frames every card
 *  @param accentColor tint for the chevron
 *  @param currentId   id of the Pokémon we are currently viewing
 *  @param onNavigate  callback when user taps another stage
 */
@Composable
fun EvolutionTab(
    stages: List<EvolutionStage>,
    borderType: String,
    accentColor: Color,
    currentId: Int,
    onNavigate: (Int) -> Unit
) {
    if (stages.isEmpty()) {
        Box(
            Modifier.fillMaxWidth().padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) { Text("No evolution data.", fontWeight = FontWeight.Bold) }
        return
    }

    Spacer(Modifier.height(24.dp))

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stages.forEachIndexed { idx, stage ->

            val isCurrent = stage.id == currentId

            EvolutionCard(
                stage      = stage,
                borderType = borderType,
                enabled    = !isCurrent,
                onClick    = { if (!isCurrent) onNavigate(stage.id) }
            )

            /* chevron between cards, except after the last one */
            if (idx != stages.lastIndex) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "next evolution",
                    tint     = accentColor,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}