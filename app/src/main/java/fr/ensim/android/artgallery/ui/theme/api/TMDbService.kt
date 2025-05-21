package fr.ensim.android.artgallery.ui.theme.api

import fr.ensim.android.artgallery.ui.theme.api.data.tMDb.MovieResponse

interface TMDbService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): MovieResponse

    @GET("tv/popular")
    suspend fun getPopularTvShows(@Query("api_key") apiKey: String): TvShowResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetails
}