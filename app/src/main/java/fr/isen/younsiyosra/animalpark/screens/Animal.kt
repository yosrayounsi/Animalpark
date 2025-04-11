package fr.isen.younsiyosra.animalpark.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

// Modèle de données Animal
data class Animal(
    val name: String = "",
    val status: String = "",
    val animal1: String = "",
    val animal2: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun AnimalScreen(navController: NavController) {
    // Déclaration d'un état mutable pour les données
    val animalList = remember { mutableStateOf<List<Animal>>(emptyList()) }

    // Récupérer les données depuis Firestore
    val db = FirebaseFirestore.getInstance()

    Log.d("Firestore", "Tentative de récupération des animaux depuis Firestore...")

    db.collection("Enclosure")
        .get()
        .addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.d("Firestore", "Aucun animal trouvé dans la collection.")
            } else {
                Log.d("Firestore", "Données récupérées avec succès.")
                val data = result.map { document ->
                    document.toObject(Animal::class.java)
                }
                Log.d("Firestore", "Nombre d'animaux récupérés: ${data.size}")
                animalList.value = data
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Erreur lors de la récupération des données: ${exception.message}")
        }

    Log.d("Firestore", "Affichage des animaux...")

    // Column to display animals and the navbar
    Column(modifier = Modifier.padding(16.dp)) {

        // Simple navbar (TopAppBar)
        TopAppBar(
            title = { Text("Animal Park") },
            actions = {
                IconButton(onClick = { navController.navigate("reviewsc") }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go to Park")
                }
            }
        )

        // Affichage des animaux
        animalList.value.forEach { animal ->
            Log.d("Firestore", "Affichage de l'animal: ${animal.name}, ${animal.status}, ${animal.animal1}, ${animal.animal2}")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp), // Ombre sous la carte
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nom: ${animal.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Status: ${animal.status}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Animal 1: ${animal.animal1}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Animal 2: ${animal.animal2}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
