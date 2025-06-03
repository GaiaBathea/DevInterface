package fr.ensim.android.artgallery.ui.theme.model

data class Artwork(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String?,
    val type: ArtworkType,
    val date: String? = null,
    val period: String? = null,
    val description: String? = null,
    val artistBiography: String? = null,
    val theme: String? = null,
    val location: String? = null,
    val technique: String? = null,
    val dimensions: String? = null,
    val source: ArtworkSource = ArtworkSource.JOCONDE
)

enum class ArtworkType {
    PAINTING, SCULPTURE, FILM, SERIES, ARCHITECTURE, PHOTO, MUSIC, DECORATIVE_ART, OTHER
}

enum class ArtworkSource {
    JOCONDE, DEEZER, MERIMEE, TMDB
}
