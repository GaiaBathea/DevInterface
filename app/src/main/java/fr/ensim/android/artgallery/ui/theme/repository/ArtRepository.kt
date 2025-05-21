package fr.ensim.android.artgallery.ui.theme.repository

import fr.ensim.android.artgallery.ui.theme.db.dao.ArtistDao
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtworkDao
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArtRepository(
    private val artworkDao: ArtworkDao,
    private val artistDao: ArtistDao
) {
    val allArtworks: Flow<List<Artwork>> = artworkDao.getAllArtworksWithArtists()
        .map { artworkWithArtistList ->
            artworkWithArtistList.map { it.toArtwork() }
        }

    fun getArtworkById(id: Long): Flow<Artwork> {
        return artworkDao.getArtworkWithArtistById(id)
            .map { it.toArtwork() }
    }

    fun getRelatedArtworks(artistId: Long, excludeArtworkId: Long): Flow<List<Artwork>> {
        return artworkDao.getRelatedArtworksByArtist(artistId, excludeArtworkId)
            .map { artworkWithArtistList ->
                artworkWithArtistList.map { it.toArtwork() }
            }
    }

    fun searchArtworks(query: String): Flow<List<Artwork>> {
        return artworkDao.searchArtworks(query)
