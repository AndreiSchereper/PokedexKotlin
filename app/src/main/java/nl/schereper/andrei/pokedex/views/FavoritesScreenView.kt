package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nl.schereper.andrei.pokedex.viewmodels.FavoritesViewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonGrid

@Composable
fun FavoritesScreenView(navController: NavHostController) {

    /* Share the same PokedexViewModel instance as the main tab */
    val owner: ViewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    val listVm: PokedexViewModel   = viewModel(viewModelStoreOwner = owner)
    val favVm: FavoritesViewModel  = viewModel()

    val favIds  by favVm.favorites.collectAsState()
    val allMons by listVm.visiblePokemon.collectAsState()

    /* Ensure every favourite Pokémon is loaded in VM cache */
    LaunchedEffect(favIds) {
        favIds.forEach { listVm.ensurePokemonLoaded(it) }
    }

    val favMons = allMons.filter { favIds.contains(it.id) }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            "Favorites",
            style      = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        if (favMons.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(
                    "No favorites yet",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        } else {
            PokemonGrid(
                vm            = listVm,
                favVm         = favVm,
                navController = navController,
                itemsOverride = favMons
            )
        }
    }
}