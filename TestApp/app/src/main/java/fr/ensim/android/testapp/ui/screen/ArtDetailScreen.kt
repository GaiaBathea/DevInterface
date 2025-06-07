package fr.ensim.android.testapp.ui.screen

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import fr.ensim.android.testapp.MessageService
import fr.ensim.android.testapp.ui.theme.TestAppTheme
import kotlinx.coroutines.launch

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
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    val scope = rememberCoroutineScope()
    var sendResult by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1126))
            .verticalScroll(rememberScrollState())
    ) {
        // Bouton fermeture
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 250.dp)
            )
        }

        // Bloc infos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color.White
                )
                Text(text = artist, color = Color.LightGray)
                Text(text = year, color = Color.Gray)

                Spacer(modifier = Modifier.height(8.dp))

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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vu/Inconnu
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            Text(text = if (seen) "Déjà vu" else "Inconnu", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Détail œuvre
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Détail sur l'œuvre", color = Color.LightGray, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent libero.",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton envoyer
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Envoyer cette œuvre")
        }

        if (showDialog) {
            SendArtworkDialog(
                onDismiss = { showDialog = false },
                onSend = { recipientEmail, message ->
                    showDialog = false
                    scope.launch {
                        val result = MessageService.sendArtworkMessage(
                            recipientEmail,
                            imageName,
                            title,
                            artist,
                            year,
                            message
                        )
                        sendResult = if (result.isSuccess) "Œuvre envoyée !" else "Erreur : ${result.exceptionOrNull()?.message}"
                    }
                }
            )
        }

        sendResult?.let {
            Text(
                text = it,
                color = if (it.startsWith("Erreur")) Color.Red else Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SendArtworkDialog(
    onDismiss: () -> Unit,
    onSend: (String, String) -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Envoyer l'œuvre") },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email du destinataire") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSend(email.text, message.text) }) {
                Text("Envoyer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ArtDetailScreenPreview() {
    TestAppTheme {
        ArtDetailScreen(
            navController = rememberNavController(),
            imageName = "art_piece_1",
            title = "Minerve",
            artist = "Andrea Mantegna",
            year = "1502"
        )
    }
}
