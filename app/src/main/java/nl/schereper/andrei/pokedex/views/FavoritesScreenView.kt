package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nl.schereper.andrei.pokedex.viewmodels.FavoritesViewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonListItem

@Composable
fun FavoritesScreenView(navController: NavHostController) {
    val favVm: FavoritesViewModel = viewModel()
    val listVm: PokedexViewModel  = viewModel()

    val favIds  by favVm.favorites.collectAsState()
    val allMons by listVm.visiblePokemon.collectAsState()
    val favMons = allMons.filter { favIds.contains(it.id) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* Title */
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        if (favMons.isEmpty()) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No favorites yet",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        } else {
            LazyVerticalGrid(
                columns              = GridCells.Fixed(2),
                verticalArrangement  = Arrangement.spacedBy(12.dp),
                horizontalArrangement= Arrangement.spacedBy(12.dp),
                contentPadding       = PaddingValues(8.dp),
                modifier             = Modifier.fillMaxSize()
            ) {
                items(favMons) { mon ->
                    PokemonListItem(
                        name             = mon.name,
                        imageUrl         = mon.imageUrl,
                        type             = mon.type,
                        id               = mon.id,
                        isFavorite       = true,
                        onToggleFavorite = { favVm.toggle(mon.id) },
                        onClick          = { navController.navigate("details/${mon.id}") }
                    )
                }
            }
        }
    }
}