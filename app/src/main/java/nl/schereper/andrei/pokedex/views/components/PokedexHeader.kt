package nl.schereper.andrei.pokedex.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Big “Pokédex” title + search TextField.
 */
@Composable
fun PokedexHeader(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Text(
        text       = "Pokédex",
        style      = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color      = MaterialTheme.colorScheme.onBackground,
        modifier   = Modifier.padding(start = 16.dp, top = 16.dp)
    )

    OutlinedTextField(
        value         = query,
        onValueChange = onQueryChange,
        singleLine    = true,
        label         = { Text("Search Pokémon") },
        modifier      = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}