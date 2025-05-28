package fr.ensim.android.artgallery.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.repository.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArtworkDetailViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {

    private val _artwork = MutableStateFlow<Artwork?>(null)
    val artwork: StateFlow<Artwork?> = _artwork.asStateFlow()

    private val _relatedArtworks = MutableStateFlow<List<Artwork>>(emptyList())
    val relatedArtworks: StateFlow<List<Artwork>> = _relatedArtworks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadArtwork(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val artwork = repository.getArtworkById(id)
                _artwork.value = artwork

                // Charger les œuvres associées
                artwork?.let { art ->
                    val related = repository.getRelatedArtworks(art.artist, art.id)
                    _relatedArtworks.value = related
                }

            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}