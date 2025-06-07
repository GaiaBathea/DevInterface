package fr.ensim.android.testapp.ui.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import fr.ensim.android.testapp.ui.theme.TestAppTheme

data class ArtworkMessage(
    val sender: String = "",
    val imageName: String = "",
    val title: String = "",
    val artist: String = "",
    val year: String = "",
    val message: String = ""
)

@Composable
fun InboxScreen() {
    var messages by remember { mutableStateOf<List<ArtworkMessage>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(true) {
        if (currentUser != null) {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("artworkMessages")
                    .whereEqualTo("recipient", currentUser.email)
                    .get()
                    .await()

                messages = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(ArtworkMessage::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1126))
            .padding(16.dp)
    ) {
        Text(
            text = "Boîte de réception",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            if (messages.isEmpty()) {
                Text(
                    text = "Aucun message reçu.",
                    color = Color.LightGray
                )
            } else {
                LazyColumn {
                    items(messages) { msg ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E213A))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "De : ${msg.sender}",
                                    color = Color.LightGray,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                val imageId = context.resources.getIdentifier(
                                    msg.imageName,
                                    "drawable",
                                    context.packageName
                                )

                                if (imageId != 0) {
                                    androidx.compose.foundation.Image(
                                        painter = painterResource(id = imageId),
                                        contentDescription = msg.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = msg.title,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontStyle = FontStyle.Italic
                                )
                                Text(text = msg.artist, color = Color.Gray)
                                Text(text = msg.year, color = Color.DarkGray)

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(text = "Message :", color = Color.LightGray)
                                Text(text = msg.message, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
