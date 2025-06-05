package fr.ensim.android.testapp.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.ensim.android.testapp.ui.theme.TestAppTheme
import androidx.compose.ui.layout.ContentScale


import androidx.compose.foundation.lazy.grid.*
import fr.ensim.android.testapp.data.JocondeFields
import fr.ensim.android.testapp.service.RetrofitClient
import fr.ensim.android.testapp.ui.component.JocondeImageCard

@Composable
fun HomeScreen(navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val imageList = listOf(
        "art_piece_1", "art_piece_2", "art_piece_3", "art_piece_4",
        "art_piece_5", "art_piece_6", "art_piece_7", "art_piece_8",
        "art_piece_9", "art_piece_10", "art_piece_11"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            IconButton(onClick = { navController.navigate("login") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
            }
        }

        // Barre de recherche
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Recherche") },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Rechercher")
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grille adaptative
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(imageList) { imageName ->
                ArtImageCard(drawableName = imageName) {
                    // Naviguer vers détail (à adapter selon chaque image)
                    navController.navigate("detail/$imageName/Title/Author/Year")
                }
            }
        }
    }
}

@Composable
fun ArtImageCard(drawableName: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageId = remember(drawableName) {
        context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

    if (imageId != 0) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f) // carré - ajuste ici si tu veux des hauteurs différentes
                .clickable { onClick() }
        )
    } else {
        Text("Image \"$drawableName\" introuvable", modifier = Modifier.padding(8.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TestAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
