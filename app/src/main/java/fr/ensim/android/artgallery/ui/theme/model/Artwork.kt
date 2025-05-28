package fr.ensim.android.artgallery.ui.theme.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.ensim.android.artgallery.ui.screens.Artwork

@Entity(tableName = "artworks")
data class Artwork(
    @PrimaryKey val id: String,
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

val homeArtworks = artworks.map { artwork ->
    Artwork(
        id = artwork.id.toString(),
        title = artwork.title ?: "Sans titre",
        artist = artwork.artist ?: "Artiste inconnu",
        imageUrl = artwork.imageUrl ?: "",
        height = (150..300).random() // Pour l'effet staggered
    )
}