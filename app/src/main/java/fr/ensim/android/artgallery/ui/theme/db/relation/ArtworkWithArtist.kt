package fr.ensim.android.artgallery.ui.theme.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.artgallery.db.entity.ArtistEntity
import com.example.artgallery.db.entity.ArtworkEntity

data class ArtworkWithArtist(
    @Embedded val artwork: ArtworkEntity,
    @Relation(
        parentColumn = "artistId",
        entityColumn = "id"
    )
    val artist: ArtistEntity
)
