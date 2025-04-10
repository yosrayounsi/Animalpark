package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.younsiyosra.animalpark.R
import kotlinx.coroutines.delay

@Composable
fun RegistrationScreen(auth: FirebaseAuth, navController: NavController) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var errorMessage by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Centrage du formulaire
    ) {
        // Image de fond
        Image(
            painter = painterResource(id = R.drawable.background_animalpark),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay sombre
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        // Formulaire d'inscription
        AnimatedVisibility(visible = isVisible, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
                    .padding(24.dp)
                    .fillMaxWidth(0.9f), // largeur max 90%
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Créer un compte",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                Divider(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                            auth.createUserWithEmailAndPassword(email.text, password.text)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate("profile")
                                    } else {
                                        errorMessage = "Échec de l'inscription : ${task.exception?.message}"
                                    }
                                }
                        } else {
                            errorMessage = "Veuillez remplir tous les champs"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("S'inscrire")
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Déjà inscrit ? Connectez-vous")
                }
            }
        }
    }
}
