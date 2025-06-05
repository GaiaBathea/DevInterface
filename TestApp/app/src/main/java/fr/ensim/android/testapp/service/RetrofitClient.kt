package fr.ensim.android.testapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val apiService: JocondeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://data.culture.gouv.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JocondeApiService::class.java)
    }
}
