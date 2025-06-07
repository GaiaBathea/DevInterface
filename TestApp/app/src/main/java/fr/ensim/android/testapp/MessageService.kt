package fr.ensim.android.testapp


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object MessageService {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun sendArtworkMessage(
        recipientEmail: String,
        imageName: String,
        title: String,
        artist: String,
        year: String,
        message: String
    ): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("Utilisateur non connecté"))

        val messageData = hashMapOf(
            "senderEmail" to currentUser.email,
            "recipientEmail" to recipientEmail,
            "imageName" to imageName,
            "title" to title,
            "artist" to artist,
            "year" to year,
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        return try {
            firestore.collection("messages").add(messageData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
