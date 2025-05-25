package nl.schereper.andrei.pokedex.views.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import nl.schereper.andrei.pokedex.models.PokemonDetails
import nl.schereper.andrei.pokedex.models.imageUrl
import nl.schereper.andrei.pokedex.utils.typeColorMap
import nl.schereper.andrei.pokedex.viewmodels.PokemonDetailsViewModel

@Composable
fun PokemonDetailsScreenView(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val vm: PokemonDetailsViewModel = viewModel()
    val pokemon by vm.pokemon.collectAsState()

    pokemon?.let { PokemonContent(it, navController, modifier) }
        ?: Box(modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
}

/* ──────────────────────────── UI ──────────────────────────── */

@Composable
private fun PokemonContent(
    p: PokemonDetails,
    nav: NavHostController,
    modifier: Modifier = Modifier
) {
    val primaryColor = typeColorMap[p.types.firstOrNull()?.type?.name?.lowercase()]
        ?: MaterialTheme.colorScheme.primary

    var tab by remember { mutableIntStateOf(0) }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 32.dp)
    ) {
        Header(p, nav)
        TabRow(
            selectedTabIndex = tab,
            containerColor   = Color.Transparent,
            // text colour fallback; real text colour below
            contentColor     = primaryColor,
            indicator        = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[tab])
                        .height(3.dp),
                    color = primaryColor
                )
            }
        ) {
            listOf("About", "Stats", "Evolution").forEachIndexed { i, t ->
                Tab(
                    selected = tab == i,
                    onClick  = { tab = i }
                ) {
                    Text(
                        t,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color      = if (tab == i) primaryColor
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier   = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
        when (tab) {
            0 -> AboutTab(p)
            1 -> StatsTab(p, primaryColor)   // ← pass colour
            else -> EvolutionTab()
        }
    }
}

/* ───────────────────────── header ───────────────────────── */

@Composable
private fun Header(
    p: PokemonDetails,
    nav: NavHostController
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { nav.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text       = p.name.replaceFirstChar { it.uppercase() },
                style      = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                modifier   = Modifier.weight(1f)
            )
            Text(
                text  = "#${p.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        /* Solid-colour chips, bold label, no border */
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            p.types.forEach { slot ->
                val typeName = slot.type.name.lowercase()
                val bgColor  = typeColorMap[typeName]
                    ?: MaterialTheme.colorScheme.primary
                val fgColor  = MaterialTheme.colorScheme.onPrimary

                AssistChip(
                    onClick = {},
                    label   = {
                        Text(
                            typeName.replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors  = AssistChipDefaults.assistChipColors(
                        containerColor = bgColor,
                        labelColor     = fgColor
                    ),
                    border  = null
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        AsyncImage(
            model = p.imageUrl,
            contentDescription = p.name,
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

/* ──────────────────────── About tab ─────────────────────── */

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

    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        rows.forEach { (label, value) ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(label, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/* ──────────────────────── Stats tab ─────────────────────── */

@Composable
private fun StatsTab(
    p: PokemonDetails,
    barColor: Color                      // ← colour from first type
) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        p.stats.forEach { stat ->
            val value = stat.base_stat.coerceAtMost(255)
            val ratio = value / 255f
            val name  = stat.stat.name.replace('-', ' ')
                .replaceFirstChar(Char::uppercase)

            /* Title row above the bar */
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text(value.toString(), fontWeight = FontWeight.SemiBold)
            }

            /* Progress bar */
            LinearProgressIndicator(
                progress   = ratio,
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color      = barColor                    // ← coloured bar
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ─────────────────── Evolution tab (placeholder) ─────────────────── */

@Composable
private fun EvolutionTab() =
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        Alignment.Center
    ) {
        Text(
            "Evolution chain coming soon…",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }