package fr.ensim.android.testapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.ensim.android.testapp.ui.theme.TestAppTheme

@Composable
fun ListeAScreen(navController: NavController) {
    val cinemaImages = listOf("art_piece_6", "art_piece_7", "art_piece_8", "art_piece_9")
    val artImages = listOf("art_piece_1", "art_piece_2", "art_piece_3", "art_piece_4", "art_piece_5")
    val photoImages = listOf("art_piece_6", "art_piece_7", "art_piece_8", "art_piece_9")
    val architectureImages = listOf("art_piece_10", "art_piece_11", "art_piece_1")

    Box(modifier = Modifier.fillMaxSize()) {

        // Bouton de fermeture
        IconButton(
            onClick = {
                navController.navigate("monboard") {
                    popUpTo("listea") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fermer"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CarouselSectionL("Cinématographie", cinemaImages, navController)
            CarouselSectionL("Œuvre", artImages, navController)
            CarouselSectionL("Photographie", photoImages, navController)
            CarouselSectionL("Architecture", architectureImages, navController)
        }
    }
}

@Composable
fun CarouselSectionL(title: String, imageList: List<String>, navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(imageList) { imageName ->
                ImageCardL(imageName = imageName) {
                    navController.navigate("detail/$imageName/Title/Author/Year")
                }
            }
        }
    }
}

@Composable
fun ImageCardL(imageName: String, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    if (imageId != 0) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 120.dp, height = 160.dp)
                .clickable { onClick() }
        )
    } else {
        Text(
            text = "Image \"$imageName\" introuvable",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListeAScreenPreview() {
    TestAppTheme {
        ListeAScreen(navController = rememberNavController())
    }
}
