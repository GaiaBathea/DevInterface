package fr.ensim.android.testapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.ensim.android.testapp.AuthService
import fr.ensim.android.testapp.ui.theme.TestAppTheme
import kotlinx.coroutines.launch

@Composable
fun ConnexionScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val fieldWidth = 280.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Bouton de fermeture en haut à droite
        IconButton(
            onClick = {
                navController.navigate("home") {
                    popUpTo("connexion") { inclusive = true }
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

        // Contenu principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Connexion",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pas de compte ? Créez-en un",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { navController.navigate("signup") }
                    .padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(fieldWidth)
            ) {
                Text("Adresse mail*", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Mot de passe*", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.width(fieldWidth)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text("  Or With  ", style = MaterialTheme.typography.bodySmall)
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Google login */ },
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(vertical = 4.dp)
                ) {
                    Text("Google")
                }

                Button(
                    onClick = { /* Facebook login */ },
                    modifier = Modifier
                        .width(fieldWidth)
                        .padding(vertical = 4.dp)
                ) {
                    Text("Facebook")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null

                    coroutineScope.launch {
                        val result = AuthService.loginUser(email, password)
                        isLoading = false
                        if (result.isSuccess) {
                            navController.navigate("monboard") {
                                popUpTo("connexion") { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            errorMessage =
                                result.exceptionOrNull()?.localizedMessage ?: "Échec de la connexion"
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.width(fieldWidth)
            ) {
                Text(if (isLoading) "Connexion..." else "Se connecter")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        // Petit bouton rond en bas à droite vers MonBoardScreen
        FloatingActionButton(
            onClick = { navController.navigate("monBoard") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            // Laisse vide ou ajoute une icône si tu veux
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnexionScreenPreview() {
    TestAppTheme {
        ConnexionScreen(navController = rememberNavController())
    }
}
