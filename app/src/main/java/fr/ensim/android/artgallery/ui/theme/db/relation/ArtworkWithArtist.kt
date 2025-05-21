package fr.ensim.android.artgallery.ui.theme.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtistEntity
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity

data class ArtworkWithArtist(
    @Embedded val artwork: ArtworkEntity,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "id"
    )
    val artist: ArtistEntity
)
