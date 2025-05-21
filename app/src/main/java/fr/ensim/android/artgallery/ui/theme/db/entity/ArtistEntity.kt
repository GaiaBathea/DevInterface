package fr.ensim.android.artgallery.ui.theme.db.entity



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val biography: String,
    val birthYear: Int,
    val deathYear: Int?
)
