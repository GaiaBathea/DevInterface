package fr.ensim.android.artgallery.ui.theme.api

import fr.ensim.android.artgallery.ui.theme.db.dao.ArtistDao
import fr.ensim.android.artgallery.ui.theme.db.dao.ArtworkDao
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtistEntity
import fr.ensim.android.artgallery.ui.theme.db.entity.ArtworkEntity

class ApiImportManager(
    private val artistDao: ArtistDao,
    private val artworkDao: ArtworkDao
) {
    // Import data from multiple sources
    suspend fun importAllData() {
        withContext(Dispatchers.IO) {
            try {
                importMoviesAndTvShows()
                importMusicAlbumsAndArtists()
                importArchitecturalMonuments()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TMDb: Import movies and TV shows
    private suspend fun importMoviesAndTvShows() {
        try {
            // Get popular movies
            val movieResponse = ApiClients.tmdbService.getPopularMovies(ApiConstants.TMDB_API_KEY)

            // Create "Movies" category artist
            val moviesArtistId = artistDao.insert(
                ArtistEntity(
                    name = "Various Directors",
                    biography = "Collection of films by various directors from around the world.",
                    birthYear = 1900,
                    deathYear = null
                )
            )

            // Add movies as artworks
            for (movie in movieResponse.results) {
                val posterUrl = if (movie.poster_path != null) {
                    ApiConstants.TMDB_POSTER_BASE_URL + movie.poster_path
                } else {
                    "assets/images/movie_placeholder.jpg"
                }

                artworkDao.insert(
                    ArtworkEntity(
                        title = movie.title,
                        artistId = moviesArtistId,
                        year = extractYearFromDate(movie.release_date),
                        description = movie.overview,
                        imageUrl = posterUrl,
                        category = "Film"
                    )
                )
            }

            // Get popular TV shows
            val tvResponse = ApiClients.tmdbService.getPopularTvShows(ApiConstants.TMDB_API_KEY)

            // Create "TV Shows" category artist
            val tvArtistId = artistDao.insert(
                ArtistEntity(
                    name = "Various TV Creators",
                    biography = "Collection of television series by various creators and networks.",
                    birthYear = 1950,
                    deathYear = null
                )
            )

            // Add TV shows as artworks
            for (show in tvResponse.results) {
                val posterUrl = if (show.poster_path != null) {
                    ApiConstants.TMDB_POSTER_BASE_URL + show.poster_path
                } else {
                    "assets/images/tv_placeholder.jpg"
                }

                artworkDao.insert(
                    ArtworkEntity(
                        title = show.name,
                        artistId = tvArtistId,
                        year = extractYearFromDate(show.first_air_date),
                        description = show.overview,
                        imageUrl = posterUrl,
                        category = "Série TV"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Deezer: Import music albums and artists
    private suspend fun importMusicAlbumsAndArtists() {
        try {
            // Get chart data with popular albums
            val chartResponse = ApiClients.deezerService.getCharts()

            // Process artists and albums
            for (albumBrief in chartResponse.albums.data) {
                // Get detailed album info
                val album = ApiClients.deezerService.getAlbum(albumBrief.id)

                // Get or create artist
                val artistId = getOrCreateArtist(album.artist.id, album.artist.name)

                // Add album as artwork
                artworkDao.insert(
                    ArtworkEntity(
                        title = album.title,
                        artistId = artistId,
                        year = extractYearFromDate(album.release_date),
                        description = "Album musical par ${album.artist.name}",
                        imageUrl = album.cover_xl,
                        category = "Musique"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Mérimée: Import architectural monuments
    private suspend fun importArchitecturalMonuments() {
        try {
            // Get monuments data
            val monumentsResponse = ApiClients.merimeeService.getMonuments(
                dataset = ApiConstants.MERIMEE_DATASET,
                rows = 50,  // Number of results to retrieve
                query = ""  // Empty for all results
            )

            // Create "Architecture" category artist
            val architectureArtistId = artistDao.insert(
                ArtistEntity(
                    name = "Patrimoine Architectural",
                    biography = "Collection de monuments et bâtiments historiques français.",
                    birthYear = 1000,
                    deathYear = null
                )
            )

            // Add monuments as artworks
            for (record in monumentsResponse.records) {
                val fields = record.fields
                val title = fields.tico
                val architects = fields.autr?.joinToString(", ") ?: "Architecte inconnu"
                val description = "Adresse: ${fields.wadrs}" +
                        "\nType: ${fields.edif ?: "Non spécifié"}" +
                        "\nArchitectes: $architects"

                val imageUrl = if (fields.photo != null && fields.photo.isNotEmpty()) {
                    fields.photo[0]
                } else {
                    "assets/images/architecture_placeholder.jpg"
                }

                val year = extractYearFromMerimeeDate(fields.dpro)

                artworkDao.insert(
                    ArtworkEntity(
                        title = title,
                        artistId = architectureArtistId,
                        year = year,
                        description = description,
                        imageUrl = imageUrl,
                        category = "Architecture"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Helper to get or create an artist from Deezer
    private suspend fun getOrCreateArtist(deezerArtistId: Int, artistName: String): Long {
        // Check if artist already exists
        val existingArtists = artistDao.searchArtists(artistName)

        // If found an exact match, return that artist's ID
        for (artist in existingArtists) {
            if (artist.name.equals(artistName, ignoreCase = true)) {
                return artist.id
            }
        }

        // If not found, fetch details and create new artist
        try {
            val artistDetails = ApiClients.deezerService.getArtist(deezerArtistId)

            return artistDao.insert(
                ArtistEntity(
                    name = artistDetails.name,
                    biography = "Artiste musical avec ${artistDetails.nb_album} albums et ${artistDetails.nb_fan} fans sur Deezer.",
                    birthYear = 0, // Not available from Deezer API
                    deathYear = null
                )
            )
        } catch (e: Exception) {
            // Fallback if we can't get details
            return artistDao.insert(
                ArtistEntity(
                    name = artistName,
                    biography = "Artiste musical.",
                    birthYear = 0,
                    deathYear = null
                )
            )
        }
    }

    // Helper to extract year from date string like "1989-04-15"
    private fun extractYearFromDate(dateString: String): Int {
        return try {
            if (dateString.length >= 4) {
                dateString.substring(0, 4).toInt()
            } else {
                0 // Default if we can't parse the year
            }
        } catch (e: Exception) {
            0
        }
    }

    // Helper to extract year from Mérimée date format (can be various formats)
    private fun extractYearFromMerimeeDate(dateString: String?): Int {
        if (dateString == null) return 0

        // Try to find a 4-digit year
        val yearRegex = "\\b(1[0-9]{3}|20[0-2][0-9])\\b".toRegex()
        val match = yearRegex.find(dateString)

        return match?.value?.toIntOrNull() ?: 0
    }
}