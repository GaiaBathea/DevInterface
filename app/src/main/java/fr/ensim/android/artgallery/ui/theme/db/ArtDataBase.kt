package fr.ensim.android.artgallery.ui.theme.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtistDao
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtworkDao
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtistEntity
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity
import fr.ensim.android.artgallery.ui.theme.db.typeconverters.Converters

@Database(
    entities = [ArtworkEntity::class, ArtistEntity::class],
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
