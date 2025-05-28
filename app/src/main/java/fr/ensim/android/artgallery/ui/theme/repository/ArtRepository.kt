package fr.ensim.android.artgallery.ui.theme.repository

import fr.ensim.android.artgallery.api.ApiClients
import fr.ensim.android.artgallery.ui.theme.api.data.joconde.JocondeArtwork
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtworkDao
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.model.ArtworkSource
import fr.ensim.android.artgallery.ui.theme.model.ArtworkType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtRepository @Inject constructor(
    private val artworkDao: ArtworkDao
) {

    private val jocondeService = ApiClients.jocondeService

    suspend fun searchArtworks(query: String, page: Int = 0): List<Artwork> = withContext(Dispatchers.IO) {
        try {
            // Recherche dans l'API Joconde
            val jocondeQuery = if (query.isBlank()) {
                "*:*"
            } else {
                "TICO:*$query* OR AUTR:*$query* OR DENO:*$query*"
            }

            val response = jocondeService.searchArtworks(
                query = jocondeQuery,
                start = page * 20,
                rows = 20
            )

            if (response.isSuccessful) {
                val artworks = response.body()?.response?.docs?.mapNotNull { jocondeArtwork ->
                    mapJocondeToArtwork(jocondeArtwork)
                } ?: emptyList()

                // Sauvegarder en cache local
                artworkDao.insertAll(artworks)

                return@withContext artworks
            } else {
                // Fallback sur les données locales
                return@withContext if (query.isBlank()) {
                    artworkDao.getAllArtworks()
                } else {
                    artworkDao.searchArtworks("%$query%")
                }
            }
        } catch (e: Exception) {
            // En cas d'erreur réseau, utiliser les données locales
            return@withContext if (query.isBlank()) {
                artworkDao.getAllArtworks()
            } else {
                artworkDao.searchArtworks("%$query%")
            }
        }
    }

    suspend fun getArtworkById(id: String): Artwork? = withContext(Dispatchers.IO) {
        // D'abord chercher en local
        artworkDao.getArtworkById(id)?.let { return@withContext it }

        // Sinon chercher dans l'API
        try {
            val response = jocondeService.searchByField("REF:$id")
            if (response.isSuccessful) {
                val artwork = response.body()?.response?.docs?.firstOrNull()?.let {
                    mapJocondeToArtwork(it)
                }
                artwork?.let { artworkDao.insert(it) }
                return@withContext artwork
            }
        } catch (e: Exception) {
            // Ignorer les erreurs réseau
        }

        return@withContext null
    }

    suspend fun getArtworksByType(type: ArtworkType): List<Artwork> = withContext(Dispatchers.IO) {
        artworkDao.getArtworksByType(type)
    }

    suspend fun getRelatedArtworks(artistName: String, currentArtworkId: String): List<Artwork> = withContext(Dispatchers.IO) {
        artworkDao.getArtworksByArtist(artistName).filter { it.id != currentArtworkId }.take(5)
    }

    private fun mapJocondeToArtwork(joconde: JocondeArtwork): Artwork? {
        val ref = joconde.reference ?: return null
        val title = joconde.title?.takeIf { it.isNotBlank() } ?: "Sans titre"
        val artist = joconde.author?.takeIf { it.isNotBlank() } ?: "Artiste inconnu"

        // Construction de l'URL d'image depuis Joconde
        val imageUrl = joconde.images?.firstOrNull()?.let { imageName ->
            "https://www.pop.culture.gouv.fr/static/resources/joconde/$imageName"
        }

        return Artwork(
            id = ref,
            title = title,
            artist = artist,
            imageUrl = imageUrl,
            type = mapDomainToType(joconde.domain, joconde.denomination),
            date = joconde.millennium,
            period = joconde.period ?: joconde.epoch,
            description = joconde.history,
            artistBiography = joconde.biography,
            theme = joconde.denomination,
            location = joconde.location,
            technique = joconde.technique,
            dimensions = joconde.dimensions,
            source = ArtworkSource.JOCONDE
        )
    }

    private fun mapDomainToType(domain: String?, denomination: String?): ArtworkType {
        val combined = "${domain?.lowercase()} ${denomination?.lowercase()}"
        return when {
            combined.contains("peinture") -> ArtworkType.PAINTING
            combined.contains("sculpture") -> ArtworkType.SCULPTURE
            combined.contains("architecture") -> ArtworkType.ARCHITECTURE
            combined.contains("photographie") || combined.contains("photo") -> ArtworkType.PHOTO
            combined.contains("arts décoratifs") || combined.contains("mobilier") -> ArtworkType.DECORATIVE_ART
            else -> ArtworkType.OTHER
        }
    }
}