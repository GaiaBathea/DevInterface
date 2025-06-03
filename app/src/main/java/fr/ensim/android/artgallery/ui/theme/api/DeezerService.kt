package fr.ensim.android.artgallery.ui.theme.api

import fr.ensim.android.artgallery.ui.theme.api.data.dDeezer.DeezerAlbum
import fr.ensim.android.artgallery.ui.theme.api.data.dDeezer.DeezerArtist
import fr.ensim.android.artgallery.ui.theme.api.data.dDeezer.DeezerChartResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DeezerService {
    @GET("chart")
    suspend fun getCharts(): DeezerChartResponse

    @GET("album/{id}")
    suspend fun getAlbum(@Path("id") albumId: Int): DeezerAlbum

    @GET("artist/{id}")
    suspend fun getArtist(@Path("id") artistId: Int): DeezerArtist
}