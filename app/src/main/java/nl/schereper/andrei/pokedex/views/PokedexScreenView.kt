package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonListItem
import nl.schereper.andrei.pokedex.views.components.VerticalScrollbar

@Composable
fun PokedexScreenView() {
    val viewModel: PokedexViewModel = viewModel()
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val endReached by viewModel.endReached.collectAsState()
    val listState = rememberLazyListState()

    // pagination trigger
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItem ->
                if (lastVisibleItem == pokemonList.lastIndex && !endReached && !isLoading) {
                    viewModel.loadPokemonList()
                }
            }
    }

    Box {
        LazyColumn(
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pokemonList) { pokemon ->
                PokemonListItem(
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    type = pokemon.type,
                    onClick = { /* TODO: Navigate to detail */ }
                )
            }

            if (isLoading) {
                item {
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