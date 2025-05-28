package fr.ensim.android.artgallery.api

import fr.ensim.android.artgallery.ui.theme.api.JocondeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApiClients {

    private const val JOCONDE_BASE_URL = "https://data.culture.gouv.fr/api/records/1.0/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val jocondeRetrofit = Retrofit.Builder()
        .baseUrl(JOCONDE_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val jocondeService: JocondeService = jocondeRetrofit.create(JocondeService::class.java)

    // Vos autres services existants...
    val deezerService: DeezerService by lazy {
        // Votre implémentation existante
    }

    val merimeeService: MerimeeService by lazy {
        // Votre implémentation existante
    }

    val tmDbService: TMDbService by lazy {
        // Votre implémentation existante
    }
}