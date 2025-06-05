package fr.ensim.android.testapp.service

import fr.ensim.android.testapp.data.JocondeResponse
import retrofit2.http.GET

interface JocondeApiService {
    @GET("api/explore/v2.1/catalog/datasets/base-joconde-extrait/exports/json")
    suspend fun getJocondeData(): JocondeResponse

}

