package nl.schereper.andrei.pokedex.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* DataStore instance */
private val Context.dataStore by preferencesDataStore("favorites")

class FavoritesRepository(private val ctx: Context) {
    private val KEY = stringSetPreferencesKey("ids")

    val favorites: Flow<Set<Int>> = ctx.dataStore.data
        .map { it[KEY]?.mapNotNull(String::toIntOrNull)?.toSet() ?: emptySet() }

    suspend fun toggle(id: Int) {
        ctx.dataStore.edit { prefs ->
            val set = prefs[KEY]?.toMutableSet() ?: mutableSetOf()
            if (set.contains(id.toString())) set.remove(id.toString())
            else set.add(id.toString())
            prefs[KEY] = set
        }
    }
}