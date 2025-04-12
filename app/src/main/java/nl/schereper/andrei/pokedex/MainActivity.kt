package nl.schereper.andrei.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import nl.schereper.andrei.pokedex.ui.theme.PokedexTheme
import nl.schereper.andrei.pokedex.views.FavoritesScreenView
import nl.schereper.andrei.pokedex.views.PokedexScreenView
import nl.schereper.andrei.pokedex.views.components.NavigationBar
import nl.schereper.andrei.pokedex.views.components.NavigationItem
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokedexTheme {
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
                            PokedexScreenView()
                        }
                        composable(NavigationItem.Favorites.route) {
                            FavoritesScreenView()
                        }
                    }
                }
            }
        }
    }
}
