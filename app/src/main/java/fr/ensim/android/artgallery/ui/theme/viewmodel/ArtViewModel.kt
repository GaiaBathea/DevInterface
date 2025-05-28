package fr.ensim.android.artgallery.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.model.ArtworkType
import fr.ensim.android.artgallery.ui.theme.repository.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArtViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {

    private val _artworks = MutableStateFlow<List<Artwork>>(emptyList())
    val artworks: StateFlow<List<Artwork>> = _artworks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 0
    private var isLoadingMore = false

    init {
        loadArtworks()
    }

    fun loadArtworks(query: String = "", loadMore: Boolean = false) {
        if (isLoadingMore && loadMore) return

        viewModelScope.launch {
            try {
                if (!loadMore) {
                    _isLoading.value = true
                    currentPage = 0
                    _artworks.value = emptyList()
                } else {
                    isLoadingMore = true
                }

                _error.value = null

                val newArtworks = repository.searchArtworks(query, currentPage)

                _artworks.value = if (loadMore) {
                    _artworks.value + newArtworks
                } else {
                    newArtworks
                }

                currentPage++

            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement: ${e.message}"
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }

    fun searchArtworks(query: String) {
        _searchQuery.value = query
        loadArtworks(query)
    }

    fun filterByType(type: ArtworkType) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _artworks.value = repository.getArtworksByType(type)
            } catch (e: Exception) {
                _error.value = "Erreur lors du filtrage: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
