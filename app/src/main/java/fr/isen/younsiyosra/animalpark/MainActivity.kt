package fr.isen.younsiyosra.animalpark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseAuth
import fr.isen.younsiyosra.animalpark.screens.LoginScreen
import fr.isen.younsiyosra.animalpark.screens.ProfileScreen
import fr.isen.younsiyosra.animalpark.screens.RegistrationScreen
import fr.isen.younsiyosra.animalpark.ui.theme.AnimalparkTheme

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        setContent {
            AnimalparkTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(auth = auth, navController = navController)
                    }
                    composable("profile") {
                        ProfileScreen(auth = auth, navController = navController)
                    }
                    composable("register") {
                        RegistrationScreen(auth = auth, navController = navController)
                    }
                }
            }
        }
    }
}
