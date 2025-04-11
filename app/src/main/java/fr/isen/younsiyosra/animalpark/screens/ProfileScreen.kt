package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.younsiyosra.animalpark.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(auth: FirebaseAuth, navController: NavController) {
    val user = auth.currentUser
    val backgroundImage: Painter = painterResource(id = R.drawable.background_animalpark)
    val logoImage: Painter = painterResource(id = R.drawable.profile_image)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf(TextFieldValue("Nom Prénom")) }
    var phone by remember { mutableStateOf(TextFieldValue("06 00 00 00 00")) }
    var bio by remember { mutableStateOf(TextFieldValue("Passionné(e) d’animaux et de nature 🐾")) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            // 🌿 Image de fond douce
            Image(
                painter = backgroundImage,
                contentDescription = "Profile Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.1f) // plus discret encore
            )

            if (user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Image de profil ronde
                    Image(
                        painter = logoImage,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = user.email ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nom complet") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Téléphone") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Modifications enregistrées ✅")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E7936))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Enregistrer", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

y@                  Button(
                        onClick = {
                            auth.signOut()
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                    ) {
                        Text("Se déconnecter", color = Color.White)
                    }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}
