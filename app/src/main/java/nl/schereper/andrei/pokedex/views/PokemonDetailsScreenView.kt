package nl.schereper.andrei.pokedex.views.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import nl.schereper.andrei.pokedex.models.PokemonDetails
import nl.schereper.andrei.pokedex.models.imageUrl
import nl.schereper.andrei.pokedex.utils.typeColorMap
import nl.schereper.andrei.pokedex.viewmodels.EvolutionStage
import nl.schereper.andrei.pokedex.viewmodels.PokemonDetailsViewModel

/* ───────────────────────── Screen entry ───────────────────────── */

@Composable
fun PokemonDetailsScreenView(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val vm: PokemonDetailsViewModel = viewModel()
    val pokemon   by vm.pokemon.collectAsState()
    val evolution by vm.evolution.collectAsState()

    pokemon?.let { PokemonContent(it, evolution, navController, modifier) }
        ?: Box(modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
}

/* ───────────────────────── Screen body ───────────────────────── */

@Composable
private fun PokemonContent(
    p: PokemonDetails,
    evo: List<EvolutionStage>,
    nav: NavHostController,
    modifier: Modifier = Modifier
) {
    val firstTypeName = p.types.firstOrNull()?.type?.name?.lowercase() ?: ""
    val primaryColor  = typeColorMap[firstTypeName] ?: MaterialTheme.colorScheme.primary

    var tab by remember { mutableIntStateOf(0) }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 32.dp)
    ) {
        Header(p, nav)

        /* ── Tabs ─────────────────────────────────────────── */
        TabRow(
            selectedTabIndex = tab,
            containerColor   = Color.Transparent,
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
                Tab(selected = tab == i, onClick = { tab = i }) {
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
            1 -> StatsTab(p, primaryColor)
            else -> EvolutionTab(
                stages    = evo,
                borderType= firstTypeName,
                accent    = primaryColor,
                nav       = nav,
                currentId = p.id
            )
        }
    }
}

/* ───────────────────────── Header ───────────────────────── */

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
                p.name.replaceFirstChar { it.uppercase() },
                style      = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                modifier   = Modifier.weight(1f)
            )
            Text(
                "#${p.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            p.types.forEach { slot ->
                val typeName = slot.type.name.lowercase()
                val bgColor  = typeColorMap[typeName] ?: MaterialTheme.colorScheme.primary
                val fgColor  = MaterialTheme.colorScheme.onPrimary

                AssistChip(
                    onClick = {},
                    label   = { Text(typeName.replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Bold) },
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

/* ───────────────────────── About tab ───────────────────────── */

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

/* ───────────────────────── Stats tab ───────────────────────── */

@Composable
private fun StatsTab(p: PokemonDetails, barColor: Color) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        p.stats.forEach { stat ->
            val value = stat.base_stat.coerceAtMost(255)
            val ratio = value / 255f
            val name  = stat.stat.name.replace('-', ' ')
                .replaceFirstChar(Char::uppercase)

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

            LinearProgressIndicator(
                progress   = ratio,
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color      = barColor
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ───────────────────────── Evolution tab ───────────────────────── */

@Composable
private fun EvolutionTab(
    stages: List<EvolutionStage>,
    borderType: String,
    accent: Color,
    nav: NavHostController,
    currentId: Int
) {
    if (stages.isEmpty()) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            Alignment.Center
        ) { Text("No evolution data.", fontWeight = FontWeight.Bold) }
        return
    }

    Spacer(Modifier.height(24.dp))

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stages.forEachIndexed { index, stage ->
            val isCurrent = stage.id == currentId
            EvolutionWideCard(
                stage      = stage,
                borderType = borderType,
                enabled    = !isCurrent,
                onClick    = {
                    if (!isCurrent) nav.navigate("details/${stage.id}")
                }
            )
            if (index != stages.lastIndex) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "evolves to",
                    tint = accent,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(vertical = 8.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

/* ─────────── Wide card component ─────────── */

@Composable
private fun EvolutionWideCard(
    stage: EvolutionStage,
    borderType: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val typeColor      = typeColorMap[borderType] ?: MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.background
    val textColor      = MaterialTheme.colorScheme.onSurface

    Card(
        onClick  = onClick,
        enabled  = enabled,
        shape    = RoundedCornerShape(16.dp),
        border   = BorderStroke(2.dp, typeColor),
        elevation= CardDefaults.cardElevation(10.dp),
        colors   = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor   = textColor,
            disabledContainerColor = containerColor,
            disabledContentColor   = textColor       // keep full opacity
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    stage.name.replaceFirstChar(Char::uppercase),
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 22.sp
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(typeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        "#${stage.id.toString().padStart(3, '0')}",
                        color      = containerColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp
                    )
                }
            }

            AsyncImage(
                model = stage.imageUrl,
                contentDescription = stage.name,
                modifier = Modifier
                    .size(116.dp)
                    .padding(start = 8.dp)
            )
        }
    }
}