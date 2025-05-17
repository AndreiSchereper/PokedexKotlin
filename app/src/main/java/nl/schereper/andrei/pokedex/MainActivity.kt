package nl.schereper.andrei.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.schereper.andrei.pokedex.ui.theme.PokedexTheme
import nl.schereper.andrei.pokedex.views.SplashScreenView
import nl.schereper.andrei.pokedex.views.components.HomeScaffold   // ← new file below

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokedexTheme(dynamicColor = false) {
                RootNavGraph()
            }
        }
    }
}

@Composable
private fun RootNavGraph() {
    val rootNav = rememberNavController()

    NavHost(
        navController = rootNav,
        startDestination = "splash"
    ) {
        /* -----------  splash  ----------- */
        composable("splash") {
            SplashScreenView(
                onFinished = {
                    rootNav.navigate("home") {
                        popUpTo("splash") { inclusive = true } // remove splash from back-stack
                    }
                }
            )
        }

        /* -----------  main scaffold  ----------- */
        composable("home") { HomeScaffold() }
    }
}