package fr.ensim.android.artgallery

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.ensim.android.artgallery.ui.theme.api.ApiImportManager
import fr.ensim.android.artgallery.ui.theme.repository.ArtRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import fr.ensim.android.artgallery.ui.theme.db.ArtDataBase

@HiltAndroidApp
class ArtGalleryApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Database instance
    val database by lazy { ArtDatabase.getDatabase(this, applicationScope) }

    // DAO instances
    val artworkDao by lazy { database.artworkDao() }
    val artistDao by lazy { database.artistDao() }

    // Repository instance
    val repository by lazy { ArtRepository(artworkDao, artistDao) }

    // API Import Manager
    private val apiImportManager by lazy { ApiImportManager(artistDao, artworkDao) }

    override fun onCreate() {
        super.onCreate()

        // Copy image assets from assets to external storage if needed
        copyAssetsToStorage()

        // Import data from APIs
        applicationScope.launch(Dispatchers.IO) {
            // First check if database is empty
            val artCount = artworkDao.getArtworkCount()

            if (artCount == 0) {
                // Import data from APIs
                apiImportManager.importAllData()
            }
        }
    }

    private fun copyAssetsToStorage() {
        // Implementation to copy artwork images from assets folder to app's storage
        // This would allow referencing them using file:// URLs

        try {
            val assetManager = assets
            val imageDir = "images"

            // Get list of images in the assets/images directory
            val imageFiles = assetManager.list(imageDir) ?: return

            // Create directory for images in app's internal storage
            val destDir = getDir("artwork_images", MODE_PRIVATE)
            if (!destDir.exists()) {
                destDir.mkdir()
            }

            // Copy each image file to internal storage
            for (filename in imageFiles) {
                val inputStream = assetManager.open("$imageDir/$filename")
                val destFile = java.io.File(destDir, filename)

                if (!destFile.exists()) {
                    val outputStream = java.io.FileOutputStream(destFile)
                    inputStream.copyTo(outputStream)
                    outputStream.close()
                }

                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}