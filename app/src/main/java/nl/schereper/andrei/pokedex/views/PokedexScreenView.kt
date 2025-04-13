package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel

@Composable
fun PokedexScreenView() {
    val viewModel: PokedexViewModel = viewModel()
    val pokemonList by viewModel.pokemonList.collectAsState()

    LazyColumn {
        items(pokemonList) { pokemon ->
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}