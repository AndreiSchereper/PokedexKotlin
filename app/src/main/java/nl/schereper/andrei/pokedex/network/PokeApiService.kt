package nl.schereper.andrei.pokedex.network

import nl.schereper.andrei.pokedex.models.*

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Thin Retrofit interface for PokeAPI.
 *
 * * All calls are `suspend` – they run on Retrofit’s IO dispatcher.
 * * Default `limit` = 20 keeps paging logic centralised in the ViewModel.
 */
interface PokeApiService {

    /* ───────── list endpoint ───────── */

    /**
     * GET /pokemon?limit=20&offset=0
     *
     * @param limit  number of entries to fetch (max 100 by API spec)
     * @param offset zero-based index of the first entry
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit")  limit:  Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    /* ───────── detail endpoints ───────── */

    /** GET /pokemon/{id} */
    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(
        @Path("id") id: Int
    ): PokemonDetails

    /** GET /pokemon-species/{id} (needed just for evolution_chain URL) */
    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): PokemonSpecies

    /** GET /evolution-chain/{id} */
    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(
        @Path("id") id: Int
    ): EvolutionChain
}