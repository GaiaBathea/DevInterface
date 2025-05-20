package fr.ensim.android.artgallery.ui.theme.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.artgallery.db.dao.ArtistDao
import com.example.artgallery.db.dao.ArtworkDao
import com.example.artgallery.db.entity.ArtistEntity
import com.example.artgallery.db.entity.ArtworkEntity
import com.example.artgallery.db.typeconverters.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ArtworkEntity::class, ArtistEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArtDatabase : RoomDatabase() {
    abstract fun artworkDao(): ArtworkDao
    abstract fun artistDao(): ArtistDao

    companion object {
        @Volatile
        private var INSTANCE: ArtDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ArtDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArtDatabase::class.java,
                    "art_database"
                )
                    .addCallback(ArtDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ArtDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.artistDao(), database.artworkDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(artistDao: ArtistDao, artworkDao: ArtworkDao) {
            // Import data from JSON file in assets
            try {
                // This will be called from the Application class where context is available
                val context = INSTANCE?.applicationContext
                context?.let {
                    val inputStream = it.assets.open("artworks_data.json")
                    val size = inputStream.available()
                    val buffer = ByteArray(size)
                    inputStream.read(buffer)
                    inputStream.close()

                    val json = String(buffer, Charsets.UTF_8)

                    // Parse the JSON using Gson or Moshi and insert into database
                    val gson = com.google.gson.Gson()
                    val artData = gson.fromJson(json, ArtData::class.java)

                    // Insert artists and keep track of their IDs
                    val artistIdMap = mutableMapOf<String, Long>()

                    for (artist in artData.artists) {
                        val artistId = artistDao.insert(
                            ArtistEntity(
                                name = artist.name,
                                biography = artist.biography,
                                birthYear = artist.birthYear,
                                deathYear = artist.deathYear
                            )
                        )
                        artistIdMap[artist.name] = artistId
                    }

                    // Insert artworks using the artist IDs
                    for (artwork in artData.artworks) {
                        val artistId = artistIdMap[artwork.artistName] ?: continue

                        artworkDao.insert(
                            ArtworkEntity(
                                title = artwork.title,
                                artistId = artistId,
                                year = artwork.year,
                                description = artwork.description,
                                imageUrl = artwork.imageUrl,
                                category = artwork.category
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // Fallback to inserting a few sample entries if JSON loading fails
                val vanGoghId = artistDao.insert(
                    ArtistEntity(
                        name = "Vincent van Gogh",
                        biography = "Dutch post-impressionist painter who is among the most famous and influential figures in the history of Western art.",
                        birthYear = 1853,
                        deathYear = 1890
                    )
                )

                artworkDao.insert(
                    ArtworkEntity(
                        title = "Starry Night",
                        artistId = vanGoghId,
                        year = 1889,
                        description = "The Starry Night is an oil on canvas painting by Dutch post-impressionist painter Vincent van Gogh.",
                        imageUrl = "https://example.com/starry_night.jpg",
                        category = "Painting"
                    )
                )
            }
        }

        // Classes for parsing JSON data
        data class ArtData(
            val artists: List<ArtistData>,
            val artworks: List<ArtworkData>
        )

        data class ArtistData(
            val name: String,
            val biography: String,
            val birthYear: Int,
            val deathYear: Int?
        )

        data class ArtworkData(
            val title: String,
            val artistName: String,
            val year: Int,
            val description: String,
            val imageUrl: String,
            val category: String
        )
    }
}
