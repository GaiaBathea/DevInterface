package fr.ensim.android.artgallery.ui.theme.api

interface DeezerService {
    @GET("chart")
    suspend fun getCharts(): DeezerChartResponse

    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Int): DeezerAlbum

    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Int): DeezerArtist
}