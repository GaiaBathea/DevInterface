package fr.ensim.android.artgallery.ui.theme.model

data class Artist(
    val id: Long,
    val name: String,
    val biography: String,
    val birthYear: Int,
    val deathYear: Int?
)