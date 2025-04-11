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

data class FeedingEnclosure(
    val id: String,
    val name: String,
    var mealTime: String = ""
)

@Composable
fun FeedingScheduleScreen(modifier: Modifier = Modifier) {
    val enclosures = remember {
        mutableStateListOf(
            FeedingEnclosure("10", "La Bergerie des reptiles"),
            FeedingEnclosure("1", "Le Vallon des cascades - 1"),
            FeedingEnclosure("2", "Le Vallon des cascades - 2"),
            FeedingEnclosure("11", "Le Belvédère"),
            FeedingEnclosure("20", "Le Plateau"),
            FeedingEnclosure("37", "Les Clairières"),
            FeedingEnclosure("32", "Le Bois des pins")
        )
    }

    val db = FirebaseFirestore.getInstance()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "🦁 Horaire de nourrissage",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 22.sp,
                color = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(enclosures) { enclosure ->
            var currentInput by remember { mutableStateOf("") }
            var errorText by remember { mutableStateOf("") }

            val isValidTime = remember(currentInput) {
                currentInput.matches(Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Enclos : ${enclosure.name}", fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = {
                            currentInput = it
                            errorText = if (it.isNotEmpty() && !isValidTime) {
                                "Format invalide. Exemple : 14:30"
                            } else ""
                        },
                        label = { Text("Heure (hh:mm)") },
                        singleLine = true,
                        isError = errorText.isNotEmpty(),
                        supportingText = {
                            if (errorText.isNotEmpty()) {
                                Text(errorText, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            // Mise à jour de l'enclos avec l'heure saisie
                            enclosure.mealTime = currentInput

                            // Enregistrement dans Firestore
                            val feedingData = hashMapOf(
                                "name" to enclosure.name,
                                "mealTime" to enclosure.mealTime
                            )

                            db.collection("feeding_enclosures")
                                .document(enclosure.id)
                                .set(feedingData)
                                .addOnSuccessListener {
                                    // Action après enregistrement réussi
                                    currentInput = ""
                                    errorText = ""
                                }
                                .addOnFailureListener {
                                    // Gestion d'erreur
                                }
                        },
                        enabled = currentInput.isNotEmpty() && isValidTime,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ajouter l'heure")
                    }

                    if (enclosure.mealTime.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("⏰ Horaire actuel : ${enclosure.mealTime}", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
