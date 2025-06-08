package nl.schereper.andrei.pokedex.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.R
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel

@Composable
fun SplashScreenView(onFinished: () -> Unit) {
    val activityOwner = LocalContext.current as ViewModelStoreOwner
    val context       = LocalContext.current
    val vm: PokedexViewModel = viewModel(viewModelStoreOwner = activityOwner)

    val firstBatch by vm.pagedList.collectAsState()

    /* wait until list + sprites are fully cached */
    LaunchedEffect(firstBatch) {
        if (firstBatch.isNotEmpty()) {
            vm.prefetchSpritesBlocking(context)
            onFinished()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.pokemonlogo),
            contentDescription = "Pokédex Logo",
            modifier = Modifier.size(300.dp)
        )
    }
}