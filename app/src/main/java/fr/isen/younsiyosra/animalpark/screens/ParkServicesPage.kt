package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.CardDefaults

// Marking the function as using an experimental API
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkServicesPage(modifier: Modifier = Modifier) {
    val services = listOf(
        "Toilettes",
        "Points d'eau",
        "Boutique",
        "Gare",
        "Trajet train",
        "Lodge",
        "Tente pédagogique",
        "Paillote",
        "Café nomade",
        "Petit café",
        "Plateaux jeux",
        "Espace pique-nique",
        "Point de vue"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Updated TopAppBar using Material3
        CenterAlignedTopAppBar(
            title = { Text("Park Services") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF6200EE), // Background color of the AppBar
                titleContentColor = Color.White // Title text color
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Use LazyColumn for scrollable content
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(services.size) { index ->
                ParkServiceItem(serviceName = services[index])
            }
        }
    }
}

@Composable
fun ParkServiceItem(serviceName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.LightGray.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            // Add an icon if necessary here
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = serviceName,
                style = TextStyle(fontSize = 18.sp, color = Color.Black)
            )
        }
    }
}

