package fr.ensim.android.artgallery.ui.theme.mapper

import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.model.ArtworkSource
import fr.ensim.android.artgallery.ui.theme.model.ArtworkType

fun ArtworkEntity.toModel(artistName: String): Artwork {
    return Artwork(
        id = id.toString(),
        title = title,
        artist = artistName,
        imageUrl = imageUrl,
        type = try {
            ArtworkType.valueOf(category.uppercase())
        } catch (e: Exception) {
            ArtworkType.OTHER
        },
        date = year.toString(),
        description = description,
        source = ArtworkSource.JOCONDE // or dynamic if needed
    )
}