package nl.schereper.andrei.pokedex.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Small singleton that exposes [apiService].
 *
 * – 30 s connect / read time-outs (safe default)
 * – Simple Gson converter (no custom config)
 * – No logging/interceptors to keep it lightweight
 */
object ApiClient {

    /** Re-usable OkHttp client. */
    private val http = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout   (30, TimeUnit.SECONDS)
        .build()

    /** Single Retrofit instance. */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(http)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Generated implementation of [PokeApiService]. */
    val apiService: PokeApiService = retrofit.create(PokeApiService::class.java)
}