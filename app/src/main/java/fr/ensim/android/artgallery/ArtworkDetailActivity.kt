package com.example.artgallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun ArtworkDetailScreen(
    artwork: Artwork?,
    relatedArtworks: List<Artwork>,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit,
    onRelatedArtworkClick: (String) -> Unit,
    onClearError: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(artwork?.title ?: "Détail de l'œuvre") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onClearError) {
                        Text("Réessayer")
                    }
                }
            }

            artwork != null -> {
                ArtworkDetailContent(
                    artwork = artwork,
                    relatedArtworks = relatedArtworks,
                    onRelatedArtworkClick = onRelatedArtworkClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun ArtworkDetailContent(
    artwork: Artwork,
    relatedArtworks: List<Artwork>,
    onRelatedArtworkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image principale
        item {
            AsyncImage(
                model = CoilImageRequest.Builder(LocalContext.current)
                    .data(artwork.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Titre et informations principales
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = artwork.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = artwork.artist,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )

                    artwork.date?.let { date ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = date,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    artwork.period?.let { period ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Période: $period",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Informations techniques
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Badge de type
                        Surface(
                            color = MaterialTheme.colors.primary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = when (artwork.type) {
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
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.primary
                            )
                        }

                        artwork.theme?.let { theme ->
                            Surface(
                                color = MaterialTheme.colors.secondary.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = theme,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                    }

                    // Informations supplémentaires
                    artwork.technique?.let { technique ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Technique: $technique",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    artwork.dimensions?.let { dimensions ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Dimensions: $dimensions",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    artwork.location?.let { location ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Localisation: $location",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        // Description de l'œuvre
        artwork.description?.let { description ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "À propos de l'œuvre",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = description,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

        // Biographie de l'artiste
        artwork.artistBiography?.let { biography ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "À propos de ${artwork.artist}",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = biography,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

        // Œuvres associées
        if (relatedArtworks.isNotEmpty()) {
            item {
                Text(
                    text = "Œuvres associées",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(relatedArtworks) { relatedArtwork ->
                        RelatedArtworkCard(
                            artwork = relatedArtwork,
                            onClick = { onRelatedArtworkClick(relatedArtwork.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RelatedArtworkCard(
    artwork: Artwork,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column {
            AsyncImage(
                model = CoilImageRequest.Builder(LocalContext.current)
                    .data(artwork.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = artwork.artist,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}