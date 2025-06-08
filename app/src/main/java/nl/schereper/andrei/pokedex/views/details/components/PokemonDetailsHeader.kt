package nl.schereper.andrei.pokedex.views.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import nl.schereper.andrei.pokedex.models.PokemonDetails
import nl.schereper.andrei.pokedex.models.imageUrl
import nl.schereper.andrei.pokedex.utils.typeColorMap

@Composable
fun PokemonDetailsHeader(
    pokemon: PokemonDetails,
    tintColor: Color,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    navController: NavHostController
) {
    val heartRed  = Color(0xFFE53935)
    val heartGrey = MaterialTheme.colorScheme.onSurfaceVariant
    val headerBg  = tintColor.copy(alpha = .18f)

    Column(
        Modifier
            .fillMaxWidth()
            .background(headerBg)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        /* Row 1: back ▸ name ▸ id */
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                pokemon.name.replaceFirstChar { it.uppercase() },
                style      = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                modifier   = Modifier.weight(1f)
            )

            Text(
                "#${pokemon.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // ← tighter spacing (was 8.dp)

        /* Row 2: chips ▸ heart */
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                pokemon.types.forEach {
                    val c = typeColorMap[it.type.name] ?: tintColor
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                it.type.name.replaceFirstChar(Char::uppercase),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = c,
                            labelColor     = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = null
                    )
                }
            }

            IconButton(onClick = onToggleFavorite, modifier = Modifier.size(36.dp)) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "toggle favorite",
                    tint     = if (isFavorite) heartRed else heartGrey,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* sprite */
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}