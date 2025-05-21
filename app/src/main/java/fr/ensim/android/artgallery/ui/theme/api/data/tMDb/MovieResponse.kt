package fr.ensim.android.artgallery.ui.theme.api.data.tMDb

data class MovieResponse(
    val results: List<Movie>,
    val page: Int,
    val total_results: Int,
    val total_pages: Int
)