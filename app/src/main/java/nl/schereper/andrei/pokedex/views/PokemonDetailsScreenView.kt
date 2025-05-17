package nl.schereper.andrei.pokedex.views.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import nl.schereper.andrei.pokedex.models.PokemonDetails
import nl.schereper.andrei.pokedex.models.imageUrl
import nl.schereper.andrei.pokedex.viewmodels.PokemonDetailsViewModel

@Composable
fun PokemonDetailsScreenView() {
    /* ViewModel gets the {id} from NavHost’s SavedStateHandle automatically */
    val vm: PokemonDetailsViewModel = viewModel()
    val pokemon by vm.pokemon.collectAsState()

    pokemon?.let { p ->
        PokemonContent(p)
    } ?: Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

/* ────────────────────────── UI  ────────────────────────── */

@Composable
private fun PokemonContent(p: PokemonDetails) {
    var tab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 32.dp)
    ) {
        Header(p)
        TabRow(
            selectedTabIndex = tab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            listOf("About", "Stats", "Evolution").forEachIndexed { i, title ->
                Tab(selected = tab == i, onClick = { tab = i }) { Text(title) }
            }
        }

        when (tab) {
            0 -> AboutTab(p)
            1 -> StatsTab(p)
            else -> EvolutionTab()
        }
    }
}

/* ───────────── header with single sprite ───────────── */

@Composable
private fun Header(p: PokemonDetails) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = p.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "#${p.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        /* type chips */
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            p.types.forEach { slot ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(slot.type.name.replaceFirstChar { it.uppercase() })
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .15f)
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        /* single official-artwork sprite */
        AsyncImage(
            model = p.imageUrl,
            contentDescription = p.name,
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

/* ─────────── About tab ─────────── */

@Composable
private fun AboutTab(p: PokemonDetails) {
    val rows = listOf(
        "Name"      to p.name.replaceFirstChar { it.uppercase() },
        "ID"        to p.id.toString().padStart(3, '0'),
        "Base Exp." to "${p.base_experience} XP",
        "Weight"    to "%.1f kg".format(p.weight / 10f),
        "Height"    to "%.1f m".format(p.height / 10f),
        "Types"     to p.types.joinToString { it.type.name.replaceFirstChar(Char::uppercase) },
        "Abilities" to p.abilities.joinToString { it.ability.name.replaceFirstChar(Char::uppercase) }
    )

    Column(Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        rows.forEach { (label, value) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/* ─────────── Stats tab ─────────── */

@Composable
private fun StatsTab(p: PokemonDetails) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        p.stats.forEach { stat ->
            val v     = stat.base_stat.coerceAtMost(255)
            val ratio = v / 255f
            Text(stat.stat.name.replace('-', ' ').replaceFirstChar(Char::uppercase))
            LinearProgressIndicator(
                progress = ratio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

/* ─────────── Evolution (placeholder) ─────────── */

@Composable
private fun EvolutionTab() {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text("Evolution chain coming soon…")
    }
}