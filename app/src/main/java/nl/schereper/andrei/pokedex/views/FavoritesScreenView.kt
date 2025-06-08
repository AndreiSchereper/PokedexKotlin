package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nl.schereper.andrei.pokedex.viewmodels.FavoritesViewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonGrid

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
            "Favorites",
            style      = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        if (favMons.isEmpty()) {
            /* empty-state text */
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(
                    "No favorites yet",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        } else {
            /* reuse shared grid */
            PokemonGrid(
                vm             = listVm,
                favVm          = favVm,
                navController  = navController,
                itemsOverride  = favMons          // ← custom list
            )
        }
    }
}