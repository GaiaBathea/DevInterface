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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale


@Composable
fun HomeScreen(navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val scrollState = rememberScrollState()

    val imageList = listOf(
        "art_piece_1", "art_piece_2", "art_piece_3", "art_piece_4", "art_piece_5",
        "art_piece_6", "art_piece_7", "art_piece_8", "art_piece_9", "art_piece_10",
        "art_piece_11"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Row avatar et barre de recherche
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate("login") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
            }

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Recherche") },
                trailingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Rechercher")
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Affichage artistique
        for (i in imageList.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                ArtImageCard("art_piece_1", Modifier.weight(1f)) {
                    navController.navigate("detail/art_piece_1/Minerve/A.Mantegna/1502")
                }
                Spacer(modifier = Modifier.width(8.dp))
                ArtImageCard("art_piece_2", Modifier.weight(1f)) {
                    navController.navigate("detail/art_piece_2/Nuit étoilée/Vincent Van Gogh/1889")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
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
                .aspectRatio(1f)
                .padding(4.dp)
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
