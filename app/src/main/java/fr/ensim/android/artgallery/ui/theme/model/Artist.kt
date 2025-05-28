package fr.ensim.android.artgallery.ui.theme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey val id: String,
    val name: String,
    val biography: String? = null,
    val birthDate: String? = null,
    val deathDate: String? = null,
    val nationality: String? = null,
    val imageUrl: String? = null
)