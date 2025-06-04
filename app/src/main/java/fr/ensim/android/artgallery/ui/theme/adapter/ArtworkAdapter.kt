package fr.ensim.android.artgallery.ui.theme.adapter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest as CoilImageRequest
import com.android.volley.toolbox.ImageRequest as VolleyImageRequest

import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.model.ArtworkType

@Composable
fun ArtworkGalleryScreen(
    artworks: List<Artwork>,
    isLoading: Boolean,
    error: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onArtworkClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    onFilterByType: (ArtworkType?) -> Unit,
    onClearError: () -> Unit
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barre de recherche
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onFilterClick = { showFilterDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Gestion des erreurs
        error?.let { errorMessage ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.12f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onClearError) {
                        Text("OK")
                    }
                }
            }
        }

        // Grille d'œuvres d'art
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(artworks) { artwork ->
                    ArtworkCard(
                        artwork = artwork,
                        onClick = { onArtworkClick(artwork.id) }
                    )
                }

                if (artworks.isNotEmpty()) {
                    item {
                        LaunchedEffect(Unit) {
                            onLoadMore()
                        }
                    }
                }
            }

            if (isLoading && artworks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    // Dialog de filtrage
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onFilterSelected = { type ->
                onFilterByType(type)
                showFilterDialog = false
            }
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Rechercher par titre, artiste, thème...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher") },
        trailingIcon = {
            IconButton(onClick = onFilterClick) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrer")
            }
        },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun ArtworkCard(
    artwork: Artwork,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column {
            // Image
            AsyncImage(
                model = CoilImageRequest.Builder(LocalContext.current)
                    .data(artwork.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // Information
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = artwork.artist,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                artwork.date?.let { date ->
                    Text(
                        text = date,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                // Badge de type
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = artwork.type.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onFilterSelected: (ArtworkType?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrer par type") },
        text = {
            LazyColumn {
                item {
                    FilterOption(
                        text = "Tous",
                        onClick = { onFilterSelected(null) }
                    )
                }
                items(ArtworkType.values()) { type ->
                    FilterOption(
                        text = when (type) {
                            ArtworkType.PAINTING -> "Peinture"
                            ArtworkType.SCULPTURE -> "Sculpture"
                            ArtworkType.ARCHITECTURE -> "Architecture"
                            ArtworkType.PHOTO -> "Photographie"
                            ArtworkType.DECORATIVE_ART -> "Arts décoratifs"
                            ArtworkType.FILM -> "Film"
                            ArtworkType.SERIES -> "Série"
                            ArtworkType.MUSIC -> "Musique"
                            ArtworkType.OTHER -> "Autre"
                        },
                        onClick = { onFilterSelected(type) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}

@Composable
fun FilterOption(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth()
        )
    }
}