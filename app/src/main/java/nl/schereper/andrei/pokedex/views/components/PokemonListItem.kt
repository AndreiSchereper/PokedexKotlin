package nl.schereper.andrei.pokedex.views.components

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

@Composable
fun PokemonListItem(
    name: String,
    imageUrl: String,
    type: String,
    id: Int,
    onClick: () -> Unit
) {
    val typeColor = typeColorMap[type.lowercase()] ?: MaterialTheme.colorScheme.primary

    // Ensure we always use the theme’s actual surface color (white in light mode, black in dark)
    val containerColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, typeColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = textColor,
            disabledContainerColor = containerColor,
            disabledContentColor = textColor
        ),
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = textColor
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(typeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "#${id.toString().padStart(3, '0')}",
                        color = containerColor, // Text is surface-colored for contrast
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier.size(150.dp)
                )
            }
        }
    }
}