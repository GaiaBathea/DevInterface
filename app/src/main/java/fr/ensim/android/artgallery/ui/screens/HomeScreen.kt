package fr.ensim.android.artgallery.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class Artwork(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val height: Int = 200
)

@Composable
fun HomeScreen(
    artworks: List<Artwork> = emptyList(),
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onArtworkClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onEmailClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profil",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onSettingsClick = onSettingsClick,
                onSearchClick = { /* Déjà sur l'écran de recherche */ },
                onEmailClick = onEmailClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            // Barre de recherche
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            // Grille d'œuvres d'art en mosaïque
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                items(artworks) { artwork ->
                    ArtworkCard(
                        artwork = artwork,
                        onClick = { onArtworkClick(artwork.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                "Recherche",
                color = Color.Gray
            )
        },
        trailingIcon = {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Rechercher"
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            backgroundColor = Color(0xFFF5F5F5)
        ),
        singleLine = true
    )
}


@Composable
fun ArtworkCard(
    artwork: Artwork,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(artwork.height.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp
    ) {
        AsyncImage(
            model = artwork.imageUrl,
            contentDescription = artwork.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
            error = painterResource(id = android.R.drawable.ic_menu_gallery)
        )
    }
}

@Composable
fun BottomNavigationBar(
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onEmailClick: () -> Unit
) {
    BottomNavigation(
        backgroundColor = Color(0xFF1A1A1A),
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Paramètres",
                    tint = Color.White
                )
            },
            selected = false,
            onClick = onSettingsClick
        )

        BottomNavigationItem(
            icon = {
                Box(
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Recherche",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            selected = true,
            onClick = onSearchClick
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Messages",
                    tint = Color.White
                )
            },
            selected = false,
            onClick = onEmailClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleArtworks = listOf(
        Artwork("1", "Van Gogh", "Vincent van Gogh", "", 250),
        Artwork("2", "Starry Night", "Vincent van Gogh", "", 180),
        Artwork("3", "Monet", "Claude Monet", "", 220),
        Artwork("4", "The Scream", "Edvard Munch", "", 300),
        Artwork("5", "Girl with Pearl", "Johannes Vermeer", "", 200),
        Artwork("6", "Impression Sunrise", "Claude Monet", "", 180),
        Artwork("7", "Wheat Field", "Vincent van Gogh", "", 160),
        Artwork("8", "Water Lilies", "Claude Monet", "", 240)
    )

    HomeScreen(
        artworks = sampleArtworks,
        searchQuery = "",
        onSearchQueryChange = {},
        onArtworkClick = {},
        onProfileClick = {},
        onSettingsClick = {},
        onEmailClick = {}
    )
}