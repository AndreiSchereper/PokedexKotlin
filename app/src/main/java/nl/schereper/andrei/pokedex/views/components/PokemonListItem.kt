package nl.schereper.andrei.pokedex.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nl.schereper.andrei.pokedex.utils.typeColorMap

@Composable
fun PokemonListItem(
    name: String,
    imageUrl: String,
    type: String,
    id: Int,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier    // allows outer padding if needed
) {
    /* ----- colour setup ----- */
    val typeColor      = typeColorMap[type.lowercase()] ?: MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val textColor      = MaterialTheme.colorScheme.onSurface
    val heartOn        = Color(0xFFE53935)
    val heartOff       = MaterialTheme.colorScheme.onSurfaceVariant
    val heartTint by remember(isFavorite) { mutableStateOf(if (isFavorite) heartOn else heartOff) }

    /* ----- card ----- */
    Card(
        onClick  = onClick,
        shape    = RoundedCornerShape(16.dp),
        border   = BorderStroke(2.dp, typeColor),
        elevation= CardDefaults.cardElevation(10.dp),
        colors   = CardDefaults.cardColors(containerColor, textColor),
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
    ) {
        Box {

            /* ─── central sprite (unchanged geometry) ─── */
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .placeholder(android.R.color.transparent)
                    .error(android.R.color.transparent)
                    .build(),
                contentDescription = name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .fillMaxHeight(0.75f)
                    .align(Alignment.Center)
                    .offset(y = 36.dp)  
            )

            /* ─── overlay: name · id pill · heart ─── */
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 6.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    name.replaceFirstChar { it.uppercase() },
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    modifier   = Modifier.weight(1f)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(typeColor)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            "#${id.toString().padStart(3, '0')}",
                            color      = containerColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    IconButton(
                        onClick  = onToggleFavorite,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            tint = heartTint,
                            contentDescription = if (isFavorite)
                                "Remove $name from favourites" else "Add $name to favourites"
                        )
                    }
                }
            }
        }
    }
}