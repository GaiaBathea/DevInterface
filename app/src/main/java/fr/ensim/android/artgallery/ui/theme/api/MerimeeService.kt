package fr.ensim.android.artgallery.ui.theme.api

interface MerimeeService {
    @GET(".")
    suspend fun getMonuments(
        @Query("dataset") dataset: String,
        @Query("rows") rows: Int,
        @Query("q") query: String
    ): MerimeeResponse
}