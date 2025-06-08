package nl.schereper.andrei.pokedex.views.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Destinations exposed in the bottom navigation bar.
 */
sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Pokedex   : NavigationItem("pokedex",   Icons.Filled.Home,     "Pokédex")
    object Favorites : NavigationItem("favorites", Icons.Filled.Favorite, "Favorites")
}