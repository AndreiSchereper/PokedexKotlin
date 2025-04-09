package nl.schereper.andrei.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import nl.schereper.andrei.pokedex.ui.theme.PokedexTheme
import nl.schereper.andrei.pokedex.views.MainScreenView
import nl.schereper.andrei.pokedex.views.SplashScreenView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme(dynamicColor = false) {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreenView(
                        onFinished = {
                            showSplash = false
                        }
                    )
                } else {
                    MainScreenView()
                }
            }
        }
    }
}
