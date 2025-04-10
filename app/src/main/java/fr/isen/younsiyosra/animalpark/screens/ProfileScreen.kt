package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.younsiyosra.animalpark.ui.theme.AnimalparkTheme
import fr.isen.younsiyosra.animalpark.R // Make sure to import your resource correctly

@Composable
fun ProfileScreen(auth: FirebaseAuth, navController: NavController) {
    val user = auth.currentUser
    val backgroundImage: Painter = painterResource(id = R.drawable.background_animalpark) // Replace with your image
    val logoImage: Painter = painterResource(id = R.drawable.profile_image) // Replace with your logo

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image stretched to fill the whole screen
        Image(
            painter = backgroundImage,
            contentDescription = "Profile background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Profile box content
        if (user != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Rounded logo
                        Image(
                            painter = logoImage,
                            contentDescription = "Profile Logo",
                            modifier = Modifier
                                .size(100.dp) // Size of the logo
                                .clip(CircleShape) // Make the logo circular
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Profile info
                        Text("Welcome, ${user.email}", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            auth.signOut()
                            navController.navigate("login") {

                            }
                        }) {
                            Text("Sign Out")
                        }
                    }
                }
            }
        } else {
            // If the user is not authenticated, navigate to the login screen
            navController.navigate("login") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}
