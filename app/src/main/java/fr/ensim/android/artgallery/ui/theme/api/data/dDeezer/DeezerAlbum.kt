package fr.ensim.android.artgallery.ui.theme.api.data.dDeezer

data class DeezerAlbum(
    val id: Int,
    val title: String,
    val cover_xl: String,
    val release_date: String,
    val artist: DeezerArtistBrief,
    val tracks: DeezerTrackList
)