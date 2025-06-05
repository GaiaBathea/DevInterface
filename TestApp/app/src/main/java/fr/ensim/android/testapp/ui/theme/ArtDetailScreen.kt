package fr.ensim.android.testapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp

import androidx.navigation.NavController



@Composable
fun ArtDetailScreen(
    navController: NavController,
    imageName: String,
    title: String,
    artist: String,
    year: String
) {
    var isFavorite by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(0) }
    var seen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1126))
            .verticalScroll(rememberScrollState())
    ) {
        // Fermer bouton
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Fermer",
                    tint = Color.White
                )
            }
        }

        // Image principale
        if (imageId != 0) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 250.dp)
            )
        }

        // Infos œuvres
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "*$title*", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                    Text(text = artist, color = Color.LightGray)
                    Text(text = year, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Étoiles
            Row {
                for (i in 1..5) {
                    IconButton(onClick = { rating = i }) {
                        Icon(
                            painter = painterResource(
                                id = if (i <= rating)
                                    android.R.drawable.btn_star_big_on
                                else
                                    android.R.drawable.btn_star_big_off
                            ),
                            contentDescription = "Étoile $i",
                            tint = Color.Yellow
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Icône vu/inconnu
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { seen = !seen }) {
                    Icon(
                        painter = painterResource(
                            id = if (seen)
                                android.R.drawable.presence_online
                            else
                                android.R.drawable.presence_invisible
                        ),
                        contentDescription = "Statut de vision",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (seen) "Déjà vu" else "Inconnu",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder pour détails
            Text(
                text = "Détail sur l'œuvre",
                color = Color.LightGray,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero.",
                color = Color.White
            )
        }
    }
}
