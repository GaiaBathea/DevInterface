package fr.ensim.android.artgallery.ui.theme.model

data class Artwork(
    val id: Long,
    val title: String,
    val artist: Artist,
    val year: Int,
    val description: String,
    val imageUrl: String,
    val category: String
)