package fr.isen.younsiyosra.animalpark.screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.younsiyosra.animalpark.R

import kotlinx.coroutines.launch

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val enclosures = listOf(
        "La Bergerie des reptiles", "Le Vallon des cascades", "Le Belvédère",
        "Le Plateau", "Les Clairières", "Le Bois des pins"
    )

    var selectedEnclosure by remember { mutableStateOf(enclosures.first()) }
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf(TextFieldValue()) }
    var expanded by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 🌿 Fond image
            Image(
                painter = painterResource(id = R.drawable.background_animalpark),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.Crop
            )

            // 🌱 Contenu principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Donner votre avis",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF4E7936)
                )

                Spacer(Modifier.height(24.dp))

                // Dropdown enclos
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedEnclosure,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Choisissez un enclos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4E7936),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        enclosures.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedEnclosure = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Étoiles avec animation
                Text("Note sur 5", color = Color(0xFF4E7936), fontSize = 16.sp)
                Row {
                    (1..5).forEach { i ->
                        val isSelected = i <= rating
                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.3f else 1f,
                            animationSpec = tween(durationMillis = 200),
                            label = ""
                        )

                        Icon(
                            imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Étoile $i",
                            tint = if (isSelected) Color(0xFFFFC107) else Color(0xFFCCCCCC),
                            modifier = Modifier
                                .size(36.dp)
                                .scale(scale)
                                .clickable { rating = i }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Champ commentaire
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Votre commentaire") },
                    placeholder = { Text("Ex : Très bel enclos, les animaux sont actifs 🐯") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4E7936),
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(Modifier.height(24.dp))

                // Bouton envoyer
                Button(
                    onClick = {
                        scope.launch {
                            // Sauvegarde dans Firestore
                            val reviewData = hashMapOf(
                                "enclosure" to selectedEnclosure,
                                "rating" to rating,
                                "comment" to comment.text,
                                "timestamp" to System.currentTimeMillis()
                            )

                            try {
                                // Sauvegarde des données dans la collection "reviews"
                                db.collection("reviews")
                                    .add(reviewData)
                                    .await()

                                snackbarHostState.showSnackbar("Avis envoyé pour $selectedEnclosure ✅")
                                // Réinitialisation des champs
                                rating = 0
                                comment = TextFieldValue()
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Erreur lors de l'envoi de l'avis ❌")
                            }
                        }
                    },
                    enabled = comment.text.isNotBlank() && rating > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E7936))
                ) {
                    Text("Envoyer", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
