package nl.schereper.andrei.pokedex.views.details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import nl.schereper.andrei.pokedex.utils.typeColorMap
import nl.schereper.andrei.pokedex.viewmodels.EvolutionStage

/**
 * One row in the Evolution list.
 *
 *  • coloured border taken from [borderType]
 *  • disabled (= greyed, not clickable) for the currently-open Pokémon
 */
@Composable
fun EvolutionCard(
    stage: EvolutionStage,
    borderType: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = typeColorMap[borderType] ?: MaterialTheme.colorScheme.primary
    val bg          = MaterialTheme.colorScheme.background
    val fg          = MaterialTheme.colorScheme.onSurface
    val bold        = FontWeight.Bold
    val semiBold    = FontWeight.SemiBold

    Card(
        onClick  = onClick,
        enabled  = enabled,
        shape    = RoundedCornerShape(16.dp),
        border   = BorderStroke(2.dp, borderColor),
        elevation= CardDefaults.cardElevation(10.dp),
        colors   = CardDefaults.cardColors(
            containerColor        = bg,
            contentColor          = fg,
            disabledContainerColor= bg,
            disabledContentColor  = fg
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* name + id pill */
            Column(Modifier.weight(1f)) {
                Text(
                    stage.name.replaceFirstChar(Char::uppercase),
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = bold,
                    fontSize   = 22.sp
                )

                Spacer(Modifier.height(4.dp))

                Box(
                    Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(borderColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        "#${stage.id.toString().padStart(3, '0')}",
                        color      = bg,
                        fontWeight = semiBold,
                        fontSize   = 14.sp
                    )
                }
            }

            /* sprite */
            AsyncImage(
                model = stage.imageUrl,
                contentDescription = stage.name,
                modifier = Modifier
                    .size(116.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}