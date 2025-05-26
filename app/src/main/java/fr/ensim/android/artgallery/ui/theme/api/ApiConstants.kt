package fr.ensim.android.artgallery.ui.theme.api

import fr.ensim.android.artgallery.ui.theme.model.Artist
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtistEntity
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtistDao
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtworkDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// API Constants
object ApiConstants {
    // TMDb
    const val TMDB_API_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_API_KEY = "YOUR_TMDB_API_KEY" // Replace with your API key
    const val TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"

    // Deezer
    const val DEEZER_API_BASE_URL = "https://api.deezer.com/"

    // Mérimée
    const val MERIMEE_API_BASE_URL = "https://data.culture.gouv.fr/api/records/1.0/search/"
    const val MERIMEE_DATASET = "merimee"
}