package fr.ensim.android.artgallery.ui.theme.db.dao

import androidx.room.*
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.model.ArtworkType

@Dao
interface ArtworkDao {

    @Query("SELECT * FROM artworks ORDER BY title ASC")
    suspend fun getAllArtworks(): List<Artwork>

    @Query("SELECT * FROM artworks WHERE id = :id")
    suspend fun getArtworkById(id: String): Artwork?

    @Query("SELECT * FROM artworks WHERE title LIKE :query OR artist LIKE :query OR theme LIKE :query ORDER BY title ASC")
    suspend fun searchArtworks(query: String): List<Artwork>

    @Query("SELECT * FROM artworks WHERE type = :type ORDER BY title ASC")
    suspend fun getArtworksByType(type: ArtworkType): List<Artwork>

    @Query("SELECT * FROM artworks WHERE artist LIKE :artistName ORDER BY title ASC")
    suspend fun getArtworksByArtist(artistName: String): List<Artwork>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artwork: Artwork)

    @Insert(onConflict = OnConflict.REPLACE)
    suspend fun insertAll(artworks: List<Artwork>)

    @Delete
    suspend fun delete(artwork: Artwork)

    @Query("DELETE FROM artworks")
    suspend fun deleteAll()
}