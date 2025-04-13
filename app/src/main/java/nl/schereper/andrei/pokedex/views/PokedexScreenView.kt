package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.schereper.andrei.pokedex.utils.extractPokemonId
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel
import nl.schereper.andrei.pokedex.views.components.PokemonListItem
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import nl.schereper.andrei.pokedex.views.components.VerticalScrollbar

@Composable
fun PokedexScreenView() {
    val viewModel: PokedexViewModel = viewModel()
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val endReached by viewModel.endReached.collectAsState()
    val listState = rememberLazyListState()

    // pagination trigger stays the same...
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
                val id = extractPokemonId(pokemon.url)
                val imageUrl =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

                PokemonListItem(
                    name = pokemon.name,
                    imageUrl = imageUrl,
                    onClick = { /* TODO */ }
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

        // Add Scrollbar on the right side
        VerticalScrollbar(
            listState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(vertical = 16.dp)
        )
    }
}