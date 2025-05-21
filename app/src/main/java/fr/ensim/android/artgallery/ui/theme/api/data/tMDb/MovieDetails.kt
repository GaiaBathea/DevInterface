package fr.ensim.android.artgallery.ui.theme.api.data.tMDb

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val runtime: Int,
    val genres: List<Genre>,
    val production_companies: List<ProductionCompany>
)