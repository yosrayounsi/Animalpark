package fr.isen.younsiyosra.animalpark.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

// Modèle de données Enclosure
data class Enclosures(
    val color: String = "",
    val status: String = "",
    val name: String = "",
    val id: String = "" // Ajout de l'ID du document pour la mise à jour
)

@SuppressLint("RememberReturnType")
@Composable
fun EnclosuresScreen() {
    // Déclaration d'un état mutable pour les données
    val enclosureList = remember { mutableStateOf<List<Enclosures>>(emptyList()) }

    // Récupérer les données depuis Firestore
    val db = FirebaseFirestore.getInstance()

    Log.d("Firestore", "Tentative de récupération des enclos depuis Firestore...")

    db.collection("Enclosure")
        .get()
        .addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.d("Firestore", "Aucun enclos trouvé dans la collection.")
            } else {
                Log.d("Firestore", "Données récupérées avec succès.")
                val data = result.map { document ->
                    val enclosure = document.toObject(Enclosures::class.java)
                    enclosure.copy(id = document.id) // Ajouter l'ID du document pour la mise à jour
                }
                Log.d("Firestore", "Nombre d'enclos récupérés: ${data.size}")
                enclosureList.value = data
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Erreur lors de la récupération des données: ${exception.message}")
        }

    Log.d("Firestore", "Affichage des enclos...")
    Column(modifier = Modifier.padding(16.dp)) {
        enclosureList.value.forEach { enclosure ->
            Log.d("Firestore", "Affichage de l'enclos: ${enclosure.name}, ${enclosure.color}, ${enclosure.status}")

            // Crée un état mutable spécifique pour le statut de chaque enclos
            val statusInput = remember { mutableStateOf(enclosure.status) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp), // Ombre sous la carte
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nom: ${enclosure.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Status: ${enclosure.status}", style = MaterialTheme.typography.bodyMedium)

                    // Champ de saisie pour modifier le status
                    TextField(
                        value = statusInput.value,
                        onValueChange = { statusInput.value = it },
                        label = { Text("Nouveau status") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )

                    // Bouton pour mettre à jour le status
                    Button(
                        onClick = {
                            // Mettre à jour le status dans Firestore pour l'enclos spécifique
                            if (statusInput.value.isNotEmpty()) {
                                db.collection("Enclosure").document(enclosure.id)
                                    .update("status", statusInput.value)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Status mis à jour avec succès.")
                                        // Mettre à jour l'état de l'enclos localement
                                        enclosureList.value = enclosureList.value.map {
                                            if (it.id == enclosure.id) {
                                                it.copy(status = statusInput.value)
                                            } else {
                                                it
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("Firestore", "Erreur lors de la mise à jour du status: ${exception.message}")
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text("Mettre à jour le status")
                    }
                }
            }
        }
    }
}
