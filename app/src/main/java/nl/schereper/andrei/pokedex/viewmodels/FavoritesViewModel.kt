package nl.schereper.andrei.pokedex.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.schereper.andrei.pokedex.utils.FavoritesRepository

class FavoritesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = FavoritesRepository(app)

    val favorites = repo.favorites
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    fun toggle(id: Int) = viewModelScope.launch { repo.toggle(id) }
}