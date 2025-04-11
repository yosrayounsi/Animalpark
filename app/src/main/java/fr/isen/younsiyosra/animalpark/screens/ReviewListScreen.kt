package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ReviewListScreen(modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    // Liste des reviews récupérées de Firestore
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }

    // Récupérer les reviews depuis Firestore
    LaunchedEffect(true) {
        scope.launch {
            try {
                // Récupérer les documents dans la collection "reviews"
                val result = db.collection("reviews").get().await()
                result.documents.map { doc ->
                    Review(
                        enclosure = doc.getString("enclosure") ?: "",
                        rating = doc.getLong("rating")?.toInt() ?: 0,
                        comment = doc.getString("comment") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }.also { reviews = it }
            } catch (e: Exception) {
                // Gérer les erreurs
                reviews = emptyList()
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Afficher la liste des reviews dans une LazyColumn
            if (reviews.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(reviews) { review ->
                        ReviewItem(review)
                    }
                }
            } else {
                // Afficher un message si aucune revue n'est disponible
                Text(
                    text = "Aucune revue disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "Enclos: ${review.enclosure}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Note: ${review.rating} / 5", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Commentaire: ${review.comment}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Publié le: ${review.timestamp}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

data class Review(
    val enclosure: String,
    val rating: Int,
    val comment: String,
    val timestamp: Long
)
