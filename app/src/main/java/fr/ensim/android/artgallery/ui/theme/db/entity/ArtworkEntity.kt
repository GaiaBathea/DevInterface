package fr.ensim.android.artgallery.ui.theme.db.entity

import androidx.room.*

@Entity(
    tableName = "artworks",
    foreignKeys = [
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("artistId")]
)
data class ArtworkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artistId: Long,
    val year: Int,
    val description: String,
    val imageUrl: String,
    val category: String
)
