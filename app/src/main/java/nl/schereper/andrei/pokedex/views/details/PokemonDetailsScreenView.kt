package nl.schereper.andrei.pokedex.views.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nl.schereper.andrei.pokedex.utils.typeColorMap
import nl.schereper.andrei.pokedex.viewmodels.*
import nl.schereper.andrei.pokedex.views.details.components.*
import nl.schereper.andrei.pokedex.views.details.tabs.*

/**
 * Top-level Details screen.
 *
 * • Retrieves a [PokemonDetailsViewModel] (per-nav-back-stack) and a
 *   shared [FavoritesViewModel] for the heart toggle.
 * • Shows a header and three tab bodies: **About · Stats · Evolution**.
 * • Entire screen is vertically scrollable, so each tab body can be a
 *   simple Column/LazyColumn without its own nested scroll.
 */
@Composable
fun PokemonDetailsScreenView(navController: NavHostController) {

    /* ---------- View-models ---------- */
    val vm   : PokemonDetailsViewModel = viewModel()
    val favVm: FavoritesViewModel      = viewModel()

    /* ---------- reactive state ---------- */
    val pokemon   by vm.pokemon.collectAsState()
    val evolution by vm.evolution.collectAsState()
    val favIds    by favVm.favorites.collectAsState()

    /* local UI state: selected tab index */
    var tab by remember { mutableIntStateOf(0) }

    /* ---------- UI ---------- */
    pokemon?.let { p ->

        /* Accent = colour of first Pokémon type (fallback: theme primary) */
        val accent = typeColorMap[p.types.first().type.name]
            ?: androidx.compose.material3.MaterialTheme.colorScheme.primary

        /* Is this Pokémon already in favourites? */
        val isFav = favIds.contains(p.id)

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())      // whole page scrolls
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                .padding(bottom = 32.dp)                    // breathing space under content
        ) {
            /* -------- Header: name · id · types · heart -------- */
            PokemonDetailsHeader(
                pokemon          = p,
                tintColor        = accent,
                isFavorite       = isFav,
                onToggleFavorite = { favVm.toggle(p.id) },
                navController    = navController
            )

            /* -------- Tab bar -------- */
            DetailsTabBar(
                tabs     = listOf("About", "Stats", "Evolution"),
                selected = tab,
                onSelect = { tab = it },
                accent   = accent
            )

            /* -------- Tab bodies -------- */
            when (tab) {
                0 -> AboutTab(p)
                1 -> StatsTab(p, barColor = accent)
                else -> EvolutionTab(
                    stages      = evolution,
                    borderType  = p.types.first().type.name,
                    accentColor = accent,
                    currentId   = p.id,
                    onNavigate  = { navController.navigate("details/$it") }
                )
            }
        }
    }
    /* Still loading → show centred progress spinner */
        ?: Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
}