package fr.ensim.android.artgallery.ui.theme.api.data.dDeezer

data class DeezerTrack(
    val id: Int,
    val title: String,
    val duration: Int,
    val preview: String,
    val artist: DeezerArtistBrief,
    val album: DeezerAlbumBrief
)