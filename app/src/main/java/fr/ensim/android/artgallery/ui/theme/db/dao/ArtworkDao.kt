package fr.ensim.android.artgallery.ui.theme.db.dao

import androidx.room.*
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity

@Dao
interface ArtworkDao {

    @Query("SELECT * FROM artworks ORDER BY title ASC")
    suspend fun getAllArtworks(): List<ArtworkEntity>

    @Query("SELECT * FROM artworks WHERE id = :id")
    suspend fun getArtworkById(id: Long): ArtworkEntity?

    @Query("SELECT * FROM artworks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY title ASC")
    suspend fun searchArtworks(query: String): List<ArtworkEntity>

    @Query("SELECT * FROM artworks WHERE category = :category ORDER BY title ASC")
    suspend fun getArtworksByCategory(category: String): List<ArtworkEntity>

    @Query("SELECT * FROM artworks WHERE artistId = :artistId ORDER BY title ASC")
    suspend fun getArtworksByArtistId(artistId: Long): List<ArtworkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artwork: ArtworkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artworks: List<ArtworkEntity>)

    @Delete
    suspend fun delete(artwork: ArtworkEntity)

    @Query("DELETE FROM artworks")
    suspend fun deleteAll()
}
