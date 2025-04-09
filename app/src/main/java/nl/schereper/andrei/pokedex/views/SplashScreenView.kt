package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.schereper.andrei.pokedex.R
import nl.schereper.andrei.pokedex.viewmodels.SplashScreenViewModel

@Composable
fun SplashScreenView(
    onFinished: () -> Unit
) {
    val splashScreenViewModel: SplashScreenViewModel = viewModel()
    val isLoading by splashScreenViewModel.isLoading.collectAsState()

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            onFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFBC39)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pokemonlogo),
            contentDescription = "Pokédex Logo",
            modifier = Modifier.size(180.dp)
        )
    }
}