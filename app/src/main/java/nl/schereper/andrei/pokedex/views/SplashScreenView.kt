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
import nl.schereper.andrei.pokedex.R
import nl.schereper.andrei.pokedex.viewmodels.PokedexViewModel

/**
 * Plain splash that waits until the **first page** and its sprites are cached,
 * then calls [onFinished].
 */
@Composable
fun SplashScreenView(onFinished: () -> Unit) {

    // Activity-scoped Pokedex VM – already starts loading in init { … }
    val owner = LocalContext.current as ViewModelStoreOwner
    val ctx   = LocalContext.current
    val vm: PokedexViewModel = viewModel(viewModelStoreOwner = owner)

    val firstPage by vm.pagedList.collectAsState()

    /* when first page arrives -> cache sprites -> navigate */
    LaunchedEffect(firstPage) {
        if (firstPage.isNotEmpty()) {
            vm.prefetchSpritesBlocking(ctx)
            onFinished()
        }
    }

    /* static logo */
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