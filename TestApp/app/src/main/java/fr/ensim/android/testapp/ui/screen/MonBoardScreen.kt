package fr.ensim.android.testapp.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCE7FF)) // couleur de fond personnalisée
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bouton Mon Board (pas de navigation pour l'instant)
            Button(
                onClick = { /* TODO: Ajouter une navigation ou action plus tard */ },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Mon Board")
            }

            // Bouton Déjà Vu
            Button(
                onClick = { navController.navigate("dejavu") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Déjà Vu")
            }

            // Bouton Liste d'attente
            Button(
                onClick = { navController.navigate("listea") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
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
