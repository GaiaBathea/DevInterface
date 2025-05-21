package fr.ensim.android.artgallery.ui.theme.api.data.tMDb

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Float
)