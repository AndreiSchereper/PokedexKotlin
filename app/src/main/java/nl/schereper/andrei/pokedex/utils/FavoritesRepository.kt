package nl.schereper.andrei.pokedex.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* ------------------------------------------------------------------
   DataStore setup
   ------------------------------------------------------------------ */

/** File name on disk: data/data/<package>/datastore/favorites.preferences_pb */
private val Context.dataStore by preferencesDataStore("favorites")

/** Single key inside that file – stores a set of Pokémon IDs (as Strings). */
private val FAVORITES_KEY = stringSetPreferencesKey("ids")

/* ------------------------------------------------------------------
   Repository
   ------------------------------------------------------------------ */

/**
 * Simple wrapper around Preference DataStore that exposes
 * a flow of favorite Pokémon IDs and a toggle helper.
 */
class FavoritesRepository(private val ctx: Context) {

    /** Hot flow that emits the current set of favourites whenever it changes. */
    val favorites: Flow<Set<Int>> = ctx.dataStore.data.map { prefs ->
        prefs[FAVORITES_KEY]
            ?.mapNotNull(String::toIntOrNull)   // safe parse
            ?.toSet()
            ?: emptySet()
    }

    /**
     * Add the [id] to favourites if absent, otherwise remove it.
     *
     * Runs on the caller’s coroutine context; usually dispatched on IO.
     */
    suspend fun toggle(id: Int) {
        ctx.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY].orEmpty().toMutableSet()
            val key = id.toString()

            if (current.contains(key)) current.remove(key)
            else                       current.add(key)

            prefs[FAVORITES_KEY] = current
        }
    }
}