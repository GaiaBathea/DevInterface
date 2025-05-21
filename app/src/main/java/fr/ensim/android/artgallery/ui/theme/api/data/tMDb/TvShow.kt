package fr.ensim.android.artgallery.ui.theme.api.data.tMDb

data class TvShow(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val first_air_date: String,
    val vote_average: Float
)