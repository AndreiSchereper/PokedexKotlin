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

/**
 * This is exactly the code you already had in MainActivity,
 * just extracted so it can sit behind the splash screen.
 */
@Composable
fun HomeScaffold() {
    val navController = rememberNavController()
    val navItems = listOf(
        NavigationItem.Pokedex,
        NavigationItem.Favorites
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                navController = navController,
                items = navItems
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Pokedex.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Pokedex.route) {
                PokedexScreenView(navController)   // pass controller down
            }
            composable(NavigationItem.Favorites.route) {
                FavoritesScreenView()
            }

            /* ← NEW detail destination */
            composable(
                route = "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments!!.getInt("id")
                PokemonDetailsScreenView()
            }
        }
    }
}