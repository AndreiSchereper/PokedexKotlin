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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonListItem
import nl.schereper.andrei.pokedex.views.components.VerticalScrollbar

@Composable
fun PokedexScreenView() {
    val viewModel: PokedexViewModel = viewModel()
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val endReached by viewModel.endReached.collectAsState()
    val listState = rememberLazyGridState()

    // pagination trigger
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItem ->
                if (lastVisibleItem == pokemonList.lastIndex && !endReached && !isLoading) {
                    viewModel.loadPokemonList()
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // 🎨 from theme
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(end = 12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            state = listState
        ) {
            items(pokemonList) { pokemon ->
                PokemonListItem(
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    type = pokemon.type,
                    id = pokemon.id,
                    onClick = { /* TODO: Navigate to detail */ }
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

        VerticalScrollbar(
            listState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = 16.dp)
        )
    }
}