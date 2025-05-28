package fr.ensim.android.artgallery.ui.theme.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtistDao
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtworkDao
import fr.ensim.android.artgallery.ui.theme.db.typeconverters.Converters
import fr.ensim.android.artgallery.ui.theme.model.Artist
import fr.ensim.android.artgallery.ui.theme.model.Artwork

@Database(
    entities = [Artwork::class, Artist::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArtDataBase : RoomDatabase() {

    abstract fun artworkDao(): ArtworkDao
    abstract fun artistDao(): ArtistDao

    companion object {
        @Volatile
        private var INSTANCE: ArtDataBase? = null

        fun getDatabase(context: Context): ArtDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArtDataBase::class.java,
                    "art_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}