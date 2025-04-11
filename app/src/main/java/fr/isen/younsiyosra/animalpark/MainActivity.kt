package fr.isen.younsiyosra.animalpark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import fr.isen.younsiyosra.animalpark.screens.*
import fr.isen.younsiyosra.animalpark.ui.theme.AnimalparkTheme


// Screens pour l'admin
sealed class AdminScreen(val route: String, val title: String, val icon: ImageVector) {
    object Enclosures : AdminScreen("enclosures", "Enclos", Icons.Default.Home)
    object Feed : AdminScreen("feed", "Repas", Icons.Default.ThumbUp)
    object Feeds : AdminScreen("feeds", "Planning", Icons.Default.FavoriteBorder)
}

// Screens pour l'utilisateur normal
sealed class UserScreen(val route: String, val title: String, val icon: ImageVector) {
    object Services : UserScreen("ParkServicesPage", "Services", Icons.Default.Home)
    object Map : UserScreen("Map", "Carte", Icons.Default.LocationOn)
    object Animal : UserScreen("animal", "Animaux", Icons.Default.Favorite)
    object Review : UserScreen("review", "Avis", Icons.Default.ThumbUp)
    object ReviewList : UserScreen("reviewsc", "Tous les avis", Icons.Default.List)
    object Profile : UserScreen("profile", "Profil", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        setContent {
            AnimalparkTheme {
                val navController = rememberNavController()
                var showBottomBar by remember { mutableStateOf(true) }
                val currentUser = auth.currentUser
                val isAdmin = currentUser?.email == "admin@gmail.com"

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = Color(0xFF8BC34A),
                                contentColor = Color.White
                            ) {
                                if (isAdmin) {
                                    val adminScreens = listOf(AdminScreen.Enclosures, AdminScreen.Feed, AdminScreen.Feeds)
                                    adminScreens.forEach { screen ->
                                        NavigationBarItem(
                                            selected = navController.currentDestination?.route == screen.route,
                                            onClick = { navController.navigate(screen.route) },
                                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                                            label = { Text(screen.title) }
                                        )
                                    }
                                } else {
                                    val userScreens = listOf(
                                        UserScreen.Services,
                                        UserScreen.Map,
                                        UserScreen.Animal,
                                        UserScreen.Review,
                                        UserScreen.ReviewList,
                                        UserScreen.Profile
                                    )
                                    userScreens.forEach { screen ->
                                        NavigationBarItem(
                                            selected = navController.currentDestination?.route == screen.route,
                                            onClick = { navController.navigate(screen.route) },
                                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                                            label = { Text(screen.title) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Auth
                        composable("login") {
                            showBottomBar = false
                            LoginScreen(auth = auth, navController = navController)
                        }
                        composable("register") {
                            showBottomBar = false
                            RegistrationScreen(auth = auth, navController = navController)
                        }

                        // ADMIN Screens
                        composable("enclosures") {
                            showBottomBar = true
                            EnclosuresScreen()
                        }
                        composable("feed") {
                            showBottomBar = true
                            FeedingScheduleScreen()
                        }
                        composable("feeds") {
                            showBottomBar = true
                            FeedingScheduleListScreen()
                        }

                        // USER Screens
                        composable("ParkServicesPage") {
                            showBottomBar = true
                            ParkServicesScreen()
                        }
                        composable("Map") {
                            showBottomBar = true
                            val context = LocalContext.current
                            AndroidView(factory = { Map(context) })
                        }
                        composable("animal") {
                            showBottomBar = true
                            AnimalScreen(navController = navController)
                        }
                        composable("review") {
                            showBottomBar = true
                            ReviewScreen()
                        }
                        composable("reviewsc") {
                            showBottomBar = true
                            ReviewListScreen()
                        }
                        composable("profile") {
                            showBottomBar = true
                            ProfileScreen(auth = auth, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
