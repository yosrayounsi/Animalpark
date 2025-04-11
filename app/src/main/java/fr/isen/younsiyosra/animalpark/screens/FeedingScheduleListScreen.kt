package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Classe AnimalFeedingSchedule avec constructeur sans argument
data class AnimalFeedingSchedule(
    val id: String = "",   // Ajout de valeurs par défaut
    val name: String = "", // Ajout de valeurs par défaut
    var mealTime: String = ""  // Ajout de valeurs par défaut
) {
    // Constructeur sans argument requis pour la désérialisation Firestore
    constructor() : this("", "", "")
}

@Composable
fun FeedingScheduleListScreen(modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()

    // Liste des enclos à afficher
    var enclosures by remember { mutableStateOf<List<AnimalFeedingSchedule>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Récupérer les données de Firestore
    LaunchedEffect(Unit) {
        try {
            db.collection("feeding_enclosures")
                .get()
                .addOnSuccessListener { result ->
                    val enclosureList = result.mapNotNull { document ->
                        val enclosure = document.toObject(AnimalFeedingSchedule::class.java)
                        println("Document récupéré: ${document.id}, Enclos: ${enclosure?.name}, Heure: ${enclosure?.mealTime}")
                        enclosure?.copy(id = document.id)
                    }
                    enclosures = enclosureList
                }
                .addOnFailureListener { exception ->
                    errorMessage = "Erreur lors de la récupération des données : ${exception.message}"
                }
                .addOnCompleteListener {
                    isLoading = false
                }
        } catch (e: Exception) {
            errorMessage = "Une erreur est survenue : ${e.message}"
            isLoading = false
        }
    }

    // Interface utilisateur
    if (isLoading) {
        // Afficher un indicateur de chargement
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "🦁 Horaire de nourrissage",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 22.sp,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items(enclosures) { enclosure ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Enclos : ${enclosure.name}", fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("⏰ Horaire : ${enclosure.mealTime}", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
