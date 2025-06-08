package nl.schereper.andrei.pokedex.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import nl.schereper.andrei.pokedex.models.PokemonListItemData
import nl.schereper.andrei.pokedex.viewmodels.FavoritesViewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel

/**
 * Re-usable grid of Pokémon cards.
 *
 * If [itemsOverride] is **null** the list comes from [vm.visiblePokemon]
 * (used on the main Pokédex tab).
 * If a list is supplied (e.g. favourites), that list is rendered instead
 * and paging logic is skipped.
 */
@Composable
fun PokemonGrid(
    vm: PokedexViewModel,
    favVm: FavoritesViewModel,
    navController: NavHostController,
    itemsOverride: List<PokemonListItemData>? = null
) {
    /* ------------ data ------------ */
    val pokemonList = itemsOverride
        ?: vm.visiblePokemon.collectAsState().value

    val isLoading = if (itemsOverride == null)
        vm.isLoading.collectAsState().value else false

    val favIds by favVm.favorites.collectAsState()

    /* ------------ scroll + paging (only when not overridden) ---- */
    val listState = rememberLazyGridState()

    if (itemsOverride == null) {
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collectLatest { idx -> idx?.let { vm.onLastVisible(it) } }
        }
    }

    /* ------------ grid UI ------------ */
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
                name             = mon.name,
                imageUrl         = mon.imageUrl,
                type             = mon.type,
                id               = mon.id,
                isFavorite       = favIds.contains(mon.id),
                onToggleFavorite = { favVm.toggle(mon.id) },
                onClick          = { navController.navigate("details/${mon.id}") }
            )
        }

        /* loading footer (only for paged list) */
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