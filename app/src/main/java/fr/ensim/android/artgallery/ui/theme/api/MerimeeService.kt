package fr.ensim.android.artgallery.ui.theme.api

import fr.ensim.android.artgallery.ui.theme.api.data.dMerimee.MerimeeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MerimeeService {
    @GET(".")
    suspend fun getMonuments(
        @Query("dataset") dataset: String,
        @Query("rows") rows: Int,
        @Query("q") query: String
    ): MerimeeResponse
}