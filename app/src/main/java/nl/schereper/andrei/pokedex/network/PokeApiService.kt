package nl.schereper.andrei.pokedex.network

import nl.schereper.andrei.pokedex.models.EvolutionChain
import nl.schereper.andrei.pokedex.models.PokemonDetails
import nl.schereper.andrei.pokedex.models.PokemonListResponse
import nl.schereper.andrei.pokedex.models.PokemonSpecies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): PokemonDetails

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpecies

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") id: Int): EvolutionChain
}