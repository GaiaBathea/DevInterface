package fr.ensim.android.artgallery.ui.theme.api

object ApiClients {
    // TMDb Client
    private val tmdbRetrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.TMDB_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tmdbService: TMDbService = tmdbRetrofit.create(TMDbService::class.java)

    // Deezer Client
    private val deezerRetrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.DEEZER_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val deezerService: DeezerService = deezerRetrofit.create(DeezerService::class.java)

    // Mérimée Client
    private val merimeeRetrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.MERIMEE_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val merimeeService: MerimeeService = merimeeRetrofit.create(MerimeeService::class.java)
}