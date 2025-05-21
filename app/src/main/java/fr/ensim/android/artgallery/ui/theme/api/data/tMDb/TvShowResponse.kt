package fr.ensim.android.artgallery.ui.theme.api.data.tMDb

data class TvShowResponse(
    val results: List<TvShow>,
    val page: Int,
    val total_results: Int,
    val total_pages: Int
)