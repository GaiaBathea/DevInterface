package fr.ensim.android.testapp


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@Composable

fun InscriptionScreen(navController: NavController) {
    val fieldWidth = 280.dp //
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Inscription",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Déjà inscrit ? Connectez-vous",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { navController.navigate("login") }
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
                visualTransformation = PasswordVisualTransformation(), //
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

        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Button(onClick = {
            scope.launch {
                val result = AuthService.registerUser(email, password)
                if (result.isSuccess) {
                    Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    Toast.makeText(context, "Erreur : ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }, modifier = Modifier.width(fieldWidth)) {
            Text("S'inscrire")
        }
    }
}


