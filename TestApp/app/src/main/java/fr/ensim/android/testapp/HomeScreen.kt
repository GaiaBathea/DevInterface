import android.widget.ImageButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import fr.ensim.android.testapp.ui.theme.TestAppTheme
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image


@Composable
fun HomeScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Bouton avatar
        IconButton(onClick = {
            navController.navigate("login")
        }) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Barre de recherche
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Rechercher") },
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(24.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Rechercher"
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Images cliquables
        ImageButton("art_piece_1", "Œuvre 1") { /* Action 1 */ }
        Spacer(modifier = Modifier.height(16.dp))
        ImageButton("art_piece_2", "Œuvre 2") { /* Action 2 */ }
        Spacer(modifier = Modifier.height(16.dp))
        ImageButton("art_piece_3", "Œuvre 3") { /* Action 3 */ }
    }
}




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TestAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}


@Composable
fun ImageButton(drawableName: String, contentDesc: String, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageId = remember(drawableName) {
        context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

    if (imageId != 0) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = contentDesc,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp)
        )
    } else {
        Text("Image \"$drawableName\" non trouvée", modifier = Modifier.padding(8.dp))
    }
}


