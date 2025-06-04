package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import nl.schereper.andrei.pokedex.viewmodels.FavoritesViewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonListItem

@Composable
fun PokedexScreenView(navController: NavHostController) {
    val vm: PokedexViewModel      = viewModel()
    val favVm: FavoritesViewModel = viewModel()

    /* UI state */
    val pokemonList by vm.visiblePokemon.collectAsState()
    val isLoading   by vm.isLoading.collectAsState()
    val endReached  by vm.endReached.collectAsState()
    val query       by vm.searchQuery.collectAsState()
    val favIds      by favVm.favorites.collectAsState()

    val listState = rememberLazyGridState()

    /* Pagination trigger (disabled while searching) */
    LaunchedEffect(listState, query) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisible ->
                if (query.isBlank() &&
                    lastVisible == pokemonList.lastIndex &&
                    !endReached && !isLoading
                ) {
                    vm.maybeLoadNextPage()
                }
            }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* Title */
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,          // ← bold
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)   // ← left-aligned
        )

        /* Search bar */
        OutlinedTextField(
            value = query,
            onValueChange = vm::onSearchQueryChange,
            singleLine = true,
            label = { Text("Search Pokémon") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        /* Grid */
        LazyVerticalGrid(
            columns              = GridCells.Fixed(2),
            state                = listState,
            verticalArrangement  = Arrangement.spacedBy(12.dp),
            horizontalArrangement= Arrangement.spacedBy(12.dp),
            contentPadding       = PaddingValues(bottom = 16.dp),
            modifier             = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(pokemonList) { mon ->
                PokemonListItem(
                    name            = mon.name,
                    imageUrl        = mon.imageUrl,
                    type            = mon.type,
                    id              = mon.id,
                    isFavorite      = favIds.contains(mon.id),
                    onToggleFavorite= { favVm.toggle(mon.id) },
                    onClick         = { navController.navigate("details/${mon.id}") }
                )
            }

            if (isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        Alignment.Center
                    ) { CircularProgressIndicator() }
                }
            }
        }
    }
}