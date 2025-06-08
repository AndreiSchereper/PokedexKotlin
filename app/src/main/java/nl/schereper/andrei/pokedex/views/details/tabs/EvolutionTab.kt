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
import androidx.compose.ui.unit.dp
import nl.schereper.andrei.pokedex.viewmodels.EvolutionStage
import nl.schereper.andrei.pokedex.views.details.components.EvolutionCard

@Composable
fun EvolutionTab(
    stages: List<EvolutionStage>,
    borderType: String,                // ← NEW param
    accentColor: Color,
    currentId: Int,
    onNavigate: (Int) -> Unit
) {
    if (stages.isEmpty()) {
        Box(
            Modifier.fillMaxWidth().padding(vertical = 32.dp),
            Alignment.Center
        ) { Text("No evolution data.", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
        return
    }

    Spacer(Modifier.height(24.dp))

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stages.forEachIndexed { idx, stage ->
            val isCurrent = stage.id == currentId
            EvolutionCard(
                stage      = stage,
                borderType = borderType,          // ← pass it through
                enabled    = !isCurrent,
                onClick    = { if (!isCurrent) onNavigate(stage.id) }
            )
            if (idx != stages.lastIndex) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "next evolution",
                    tint = accentColor,
                    modifier = Modifier.size(48.dp).padding(vertical = 8.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}