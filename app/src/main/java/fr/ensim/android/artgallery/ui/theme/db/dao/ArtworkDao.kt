package fr.ensim.android.artgallery.ui.theme.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity
import fr.ensim.android.artgallery.ui.theme.db.relation.ArtworkWithArtist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artwork: ArtworkEntity): Long

    @Transaction
    @Query("SELECT * FROM artworks WHERE id = :id")
    fun getArtworkWithArtistById(id: Long): Flow<ArtworkWithArtist>

    @Transaction
    @Query("SELECT * FROM artworks")
    fun getAllArtworksWithArtists(): Flow<List<ArtworkWithArtist>>

    @Transaction
    @Query("SELECT * FROM artworks WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchArtworksByTitleOrCategory(query: String): Flow<List<ArtworkWithArtist>>

    @Transaction
    @Query("SELECT * FROM artworks WHERE artistId = :artistId AND id != :excludeId LIMIT 5")
    fun getRelatedArtworksByArtist(artistId: Long, excludeId: Long): Flow<List<ArtworkWithArtist>>

    @Transaction
    @Query("SELECT a.* FROM artworks a JOIN artists ar ON a.artistId = ar.id " +
            "WHERE ar.name LIKE '%' || :query || '%' OR a.title LIKE '%' || :query || '%' OR " +
            "a.category LIKE '%' || :query || '%'")
    fun searchArtworks(query: String): Flow<List<ArtworkWithArtist>>

    @Query("SELECT COUNT(*) FROM artworks")
    suspend fun getArtworkCount(): Int

    @Query("SELECT * FROM artworks WHERE category = :category LIMIT 20")
    fun getArtworksByCategory(category: String): Flow<List<ArtworkWithArtist>>
}
}
