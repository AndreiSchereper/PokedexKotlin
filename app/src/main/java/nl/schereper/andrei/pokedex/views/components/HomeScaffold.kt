package nl.schereper.andrei.pokedex.views.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nl.schereper.andrei.pokedex.views.FavoritesScreenView
import nl.schereper.andrei.pokedex.views.PokedexScreenView
import nl.schereper.andrei.pokedex.views.details.PokemonDetailsScreenView

/* ──────────────────────────────────────── */
/*  Route names in one place               */
/*  ───────────────────────────────────── */
private object Routes {
    const val Pokedex  = "pokedex"
    const val Favorites = "favorites"
    const val Details  = "details/{id}"
}

/**
 * Bottom-nav scaffold shown after the splash screen.
 *
 * Tabs:
 *  • Pokédex (grid + search)
 *  • Favourites
 *
 * A third destination **Details** lives outside the bar but re-uses the
 * same `NavController`, so back navigation behaves naturally.
 */
@Composable
fun HomeScaffold() {
    val navController = rememberNavController()

    /* bottom-bar items */
    val navItems = listOf(
        NavigationItem.Pokedex,
        NavigationItem.Favorites
    )

    Scaffold(
        bottomBar = {
            NavigationBar(navController, navItems)
        }
    ) { innerPadding ->

        /* ----- per-tab navigation graph ----- */
        NavHost(
            navController = navController,
            startDestination = Routes.Pokedex,
            modifier = Modifier.padding(innerPadding)
        ) {

            /* Pokédex grid */
            composable(Routes.Pokedex) {
                PokedexScreenView(navController)
            }

            /* Favourites grid */
            composable(Routes.Favorites) {
                FavoritesScreenView(navController)
            }

            /* Details */
            composable(
                route = Routes.Details,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                PokemonDetailsScreenView(navController)
            }
        }
    }
}