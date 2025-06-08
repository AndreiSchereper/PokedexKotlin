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

@Composable
fun PokemonDetailsScreenView(navController: NavHostController) {
    val vm   : PokemonDetailsViewModel = viewModel()
    val favVm: FavoritesViewModel      = viewModel()

    val pokemon   by vm.pokemon.collectAsState()
    val evolution by vm.evolution.collectAsState()
    val favIds    by favVm.favorites.collectAsState()

    var tab by remember { mutableIntStateOf(0) }

    pokemon?.let { p ->
        val primary = typeColorMap[p.types.first().type.name] ?: androidx.compose.material3.MaterialTheme.colorScheme.primary
        val isFav   = favIds.contains(p.id)

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                .padding(bottom = 32.dp)
        ) {
            PokemonDetailsHeader(
                pokemon        = p,
                tintColor      = primary,
                isFavorite     = isFav,
                onToggleFavorite = { favVm.toggle(p.id) },
                navController  = navController
            )

            DetailsTabBar(
                tabs       = listOf("About", "Stats", "Evolution"),
                selected   = tab,
                onSelect   = { tab = it },
                accent     = primary
            )

            when (tab) {
                0 -> AboutTab(p)
                1 -> StatsTab(p, barColor = primary)
                else -> EvolutionTab(
                    stages      = evolution,
                    borderType  = p.types.first().type.name,
                    accentColor = primary,
                    currentId   = p.id,
                    onNavigate  = { navController.navigate("details/$it") }
                )
            }
        }
    } ?: Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
}