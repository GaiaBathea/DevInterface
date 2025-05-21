package fr.ensim.android.artgallery.ui.theme.api.data.dDeezer

data class DeezerChartResponse(
    val tracks: DeezerTrackList,
    val albums: DeezerAlbumList,
    val artists: DeezerArtistList
)