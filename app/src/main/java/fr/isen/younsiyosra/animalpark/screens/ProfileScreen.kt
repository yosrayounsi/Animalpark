package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.younsiyosra.animalpark.ui.theme.AnimalparkTheme

@Composable
fun ProfileScreen(auth: FirebaseAuth, navController: NavController) {
    val user = auth.currentUser

    if (user != null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profile", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Welcome, ${user.email}")
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                auth.signOut()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true } // Make sure the user cannot go back to the profile screen
                }
            }) {
                Text("Sign Out")
            }
        }
    } else {
        // If user is not authenticated, navigate to the login screen directly
        navController.navigate("login") {
            popUpTo("login") { inclusive = true }
        }
    }
}
