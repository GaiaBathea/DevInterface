package fr.ensim.android.artgallery.ui.theme.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val biography: String? = null,

    // Mapper les colonnes de la DB vers vos propriétés souhaitées
    @ColumnInfo(name = "birthDate")
    val birthYear: String? = null, // ou String si c'est une date complète

    @ColumnInfo(name = "deathDate")
    val deathYear: String? = null, // ou String si c'est une date complète

    val nationality: String? = null,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String? = null
)