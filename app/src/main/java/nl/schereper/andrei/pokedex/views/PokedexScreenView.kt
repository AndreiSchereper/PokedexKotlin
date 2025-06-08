package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nl.schereper.andrei.pokedex.viewmodels.FavoritesViewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokedexHeader
import nl.schereper.andrei.pokedex.views.components.PokemonGrid

/**
 * Screen that hosts the title, search bar and grid.
 *
 * Shared [PokedexViewModel] instance is retrieved from the activity so
 * the splash screen works with the same data.
 */
@Composable
fun PokedexScreenView(navController: NavHostController) {

    /* shared VMs --------------------------------------------------- */
    val owner: ViewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    val vm: PokedexViewModel       = viewModel(viewModelStoreOwner = owner)
    val favVm: FavoritesViewModel  = viewModel()

    /* UI ----------------------------------------------------------- */
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PokedexHeader(
            query         = vm.searchQuery.collectAsState().value,
            onQueryChange = vm::onSearchQueryChange
        )

        PokemonGrid(
            vm            = vm,
            favVm         = favVm,
            navController = navController
        )
    }
}