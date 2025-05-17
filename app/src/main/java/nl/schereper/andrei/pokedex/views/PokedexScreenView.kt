package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonListItem
import nl.schereper.andrei.pokedex.views.components.VerticalScrollbar

@Composable
fun PokedexScreenView() {
    val viewModel: PokedexViewModel = viewModel()

    /* collect the *filtered* list, not the raw list */
    val pokemonList by viewModel.visiblePokemon.collectAsState()
    val isLoading   by viewModel.isLoading.collectAsState()
    val endReached  by viewModel.endReached.collectAsState()
    val query       by viewModel.searchQuery.collectAsState()

    val listState   = rememberLazyGridState()

    /* ────── pagination trigger; disabled while searching ────── */
    LaunchedEffect(listState, query) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastVisibleItem ->
                if (query.isBlank() &&
                    lastVisibleItem == pokemonList.lastIndex &&
                    !endReached && !isLoading
                ) {
                    viewModel.maybeLoadNextPage()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /* ────────────── search bar ────────────── */
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            label = { Text("Search Pokémon") }
        )

        Spacer(Modifier.height(4.dp))

        /* ─────────────── grid ─────────────── */
        Box(Modifier.weight(1f)) {
            LazyVerticalGrid(
                columns              = GridCells.Fixed(2),
                state                = listState,
                verticalArrangement  = Arrangement.spacedBy(12.dp),
                horizontalArrangement= Arrangement.spacedBy(12.dp),
                modifier             = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp)
            ) {
                items(pokemonList) { pokemon ->
                    PokemonListItem(
                        name     = pokemon.name,
                        imageUrl = pokemon.imageUrl,
                        type     = pokemon.type,
                        id       = pokemon.id,
                        onClick  = { /* TODO: Navigate to detail */ }
                    )
                }

                if (isLoading) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            /* overlay scrollbar */
            VerticalScrollbar(
                listState = listState,
                modifier  = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(vertical = 16.dp)
            )
        }
    }
}