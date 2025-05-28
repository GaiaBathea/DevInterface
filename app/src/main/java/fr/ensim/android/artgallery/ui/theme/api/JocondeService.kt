package fr.ensim.android.artgallery.ui.theme.api

import androidx.room.Query
import com.android.volley.Response
import fr.ensim.android.artgallery.ui.theme.api.data.joconde.JocondeResponse
import retrofit2.http.GET

interface JocondeService {

    @GET("opendata/search")
    suspend fun searchArtworks(
        @Query("q") query: String = "*:*",
        @Query("rows") rows: Int = 20,
        @Query("start") start: Int = 0,
        @Query("wt") format: String = "json"
    ): Response<JocondeResponse>

    @GET("opendata/search")
    suspend fun searchByField(
        @Query("q") field: String,
        @Query("fq") filter: String? = null,
        @Query("rows") rows: Int = 20,
        @Query("start") start: Int = 0,
        @Query("wt") format: String = "json"
    ): Response<JocondeResponse>
}