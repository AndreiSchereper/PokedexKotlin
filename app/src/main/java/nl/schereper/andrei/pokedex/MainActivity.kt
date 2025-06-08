package nl.schereper.andrei.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.schereper.andrei.pokedex.ui.theme.PokedexTheme
import nl.schereper.andrei.pokedex.views.SplashScreenView
import nl.schereper.andrei.pokedex.views.components.HomeScaffold

/** Small holder for route names so we avoid typos. */
private object Routes {
    const val Splash = "splash"
    const val Home   = "home"
}

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

/**
 * Top-level navigation host.
 *
 * * Starts at [Routes.Splash].
 * * Once the splash calls `onFinished`, we navigate to [Routes.Home] and
 *   **pop** the splash so it can’t be reached via the back button.
 */
@Composable
private fun RootNavGraph() {
    val rootNav = rememberNavController()

    NavHost(
        navController    = rootNav,
        startDestination = Routes.Splash
    ) {

        /* ─── Splash ─── */
        composable(Routes.Splash) {
            SplashScreenView(
                onFinished = {
                    rootNav.navigate(Routes.Home) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            )
        }

        /* ─── Main scaffold ─── */
        composable(Routes.Home) { HomeScaffold() }
    }
}