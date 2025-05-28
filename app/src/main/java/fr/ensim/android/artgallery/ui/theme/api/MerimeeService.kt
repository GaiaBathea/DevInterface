package fr.ensim.android.artgallery.ui.theme.api

import fr.ensim.android.artgallery.ui.theme.api.data.dMerimee.MerimeeResponse

interface MerimeeService {
    @GET(".")
    suspend fun getMonuments(
        @Query("dataset") dataset: String,
        @Query("rows") rows: Int,
        @Query("q") query: String
    ): MerimeeResponse
}