package fr.ensim.android.artgallery.ui.theme.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: ArtistEntity): Long

    @Query("SELECT * FROM artists WHERE id = :id")
    fun getArtistById(id: Long): Flow<ArtistEntity>

    @Query("SELECT * FROM artists")
    fun getAllArtists(): Flow<List<ArtistEntity>>

    @Query("SELECT * FROM artists WHERE name LIKE '%' || :query || '%'")
    fun searchArtists(query: String): Flow<List<ArtistEntity>>
}