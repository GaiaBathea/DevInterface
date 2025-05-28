package fr.ensim.android.artgallery


import android.content.res.Resources
import androidx.hilt.navigation.compose.hiltViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.artgallery.ArtworkDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import fr.ensim.android.artgallery.ui.screens.HomeScreen
import fr.ensim.android.artgallery.ui.theme.ArtGalleryTheme
import fr.ensim.android.artgallery.ui.theme.adapter.ArtworkGalleryScreen
//import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.viewmodel.ArtViewModel
import fr.ensim.android.artgallery.ui.theme.viewmodel.ArtworkDetailViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtGalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtGalleryApp()
                }
            }
        }
    }
}


@Composable
fun ArtGalleryApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Page d'accueil
        composable("home") {
            HomeScreenContainer(navController)
        }

        // Galerie existante
        composable("gallery") {
            GalleryScreen(navController)
        }

        // Détail d'œuvre
        composable("artwork_detail/{artworkId}") { backStackEntry ->
            val artworkId = backStackEntry.arguments?.getString("artworkId") ?: ""
            ArtworkDetailScreenContainer(
                artworkId = artworkId,
                navController = navController
            )
        }
    }
}

@Composable
fun HomeScreenContainer(navController: androidx.navigation.NavHostController) {
    val viewModel: ArtViewModel = hiltViewModel()
    val artworks by viewModel.artworks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Convertir les données du ViewModel vers le format HomeScreen
    val homeArtworks = artworks.map { artwork ->
        fr.ensim.android.artgallery.ui.screens.Artwork(
            id = artwork.id.toString(),
            title = artwork.title ?: "Sans titre",
            artist = artwork.artist ?: "Artiste inconnu",
            imageUrl = artwork.imageUrl ?: "",
            height = (150..300).random() // Hauteur aléatoire pour l'effet staggered
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadArtworks()
    }

    HomeScreen(
        artworks = homeArtworks,
        searchQuery = searchQuery,
        onSearchQueryChange = { query ->
            viewModel.searchArtworks(query)
        },
        onArtworkClick = { artworkId ->
            navController.navigate("artwork_detail/$artworkId")
        },
        onProfileClick = {
            // Navigation vers profil (à implémenter)
        },
        onSettingsClick = {
            // Navigation vers paramètres (à implémenter)
        },
        onEmailClick = {
            // Navigation vers email/contact (à implémenter)
        }
    )
}

@Composable
fun GalleryScreen(navController: androidx.navigation.NavHostController) {
    val viewModel: ArtViewModel = hiltViewModel()
    val artworks by viewModel.artworks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    ArtworkGalleryScreen(
        artworks = artworks,
        isLoading = isLoading,
        error = error,
        searchQuery = searchQuery,
        onSearchQueryChange = { query ->
            viewModel.searchArtworks(query)
        },
        onArtworkClick = { artworkId ->
            navController.navigate("artwork_detail/$artworkId")
        },
        onLoadMore = {
            viewModel.loadArtworks(searchQuery, loadMore = true)
        },
        onFilterByType = { type ->
            if (type != null) {
                viewModel.filterByType(type)
            } else {
                viewModel.loadArtworks()
            }
        },
        onClearError = {
            viewModel.clearError()
        }
    )
}



@Composable
fun ArtworkDetailScreenContainer(
    artworkId: String,
    navController: androidx.navigation.NavHostController
) {
    val viewModel: ArtworkDetailViewModel = viewModel()
    val artwork by viewModel.artwork.collectAsState()
    val relatedArtworks by viewModel.relatedArtworks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(artworkId) {
        viewModel.loadArtwork(artworkId)
    }

    ArtworkDetailScreen(
        artwork = artwork,
        relatedArtworks = relatedArtworks,
        isLoading = isLoading,
        error = error,
        onBackClick = {
            navController.popBackStack()
        },
        onRelatedArtworkClick = { relatedArtworkId ->
            navController.navigate("artwork_detail/$relatedArtworkId")
        },
        onClearError = {
            viewModel.clearError()
        }
    )
}
