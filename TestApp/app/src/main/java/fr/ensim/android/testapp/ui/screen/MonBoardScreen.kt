package fr.ensim.android.testapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MonBoardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCE7FF))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Ligne du haut avec bouton mail à gauche
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("inbox") }) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Boîte de réception"
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Mon Board", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Contenu principal centré
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* TODO: action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Mon Board")
            }
            Button(
                onClick = { navController.navigate("dejavu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Déjà Vu")
            }
            Button(
                onClick = { navController.navigate("listea") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Liste d’attente")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MonBoardScreenPreview() {
    MonBoardScreen(navController = rememberNavController())
}