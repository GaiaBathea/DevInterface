package fr.ensim.android.testapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun InscriptionScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val fieldWidth = 280.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Close button in top-right
        IconButton(
            onClick = {
                navController.navigate("home") {
                    popUpTo("signup") { inclusive = true }
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
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Créer un compte",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Déjà inscrit ? Connectez-vous",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { navController.navigate("connexion") }
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

                Spacer(modifier = Modifier.height(16.dp))

                Text("Confirmer le mot de passe*", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    errorMessage = null
                    if (password != confirmPassword) {
                        errorMessage = "Les mots de passe ne correspondent pas"
                        return@Button
                    }

                    isLoading = true
                    coroutineScope.launch {
                        val result = AuthService.registerUser(email, password)
                        isLoading = false
                        if (result.isSuccess) {
                            navController.navigate("home") {
                                popUpTo("signup") { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Échec de l'inscription"
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.width(fieldWidth)
            ) {
                Text(if (isLoading) "Création..." else "Créer un compte")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InscriptionScreenPreview() {
    TestAppTheme {
        InscriptionScreen(navController = rememberNavController())
    }
}
