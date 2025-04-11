package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkServicesScreen(modifier: Modifier = Modifier) {
    val services = listOf(
        "Toilettes" to "Disponibles à plusieurs endroits du parc.",
        "Points d'eau" to "Eau potable gratuite à disposition.",
        "Boutique" to "Achetez des souvenirs du parc.",
        "Gare" to "Point de départ du train touristique.",
        "Trajet train" to "Visite commentée de tout le parc.",
        "Lodge" to "Zone d’hébergement pour les visiteurs.",
        "Tente pédagogique" to "Activités éducatives pour les enfants.",
        "Paillote" to "Petite cabane pour se reposer.",
        "Café nomade" to "Café mobile avec boissons fraîches.",
        "Petit café" to "Endroit chaleureux pour une pause.",
        "Plateaux jeux" to "Jeux de société en libre accès.",
        "Espace pique-nique" to "Aire de repos et déjeuner.",
        "Point de vue" to "Vue panoramique sur la nature."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFFAF1)) // Vert doux en fond
            .padding(16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text("Services du Parc", fontWeight = FontWeight.Bold)
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF81C784),
                titleContentColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(services.size) { index ->
                ServiceCard(
                    serviceName = services[index].first,
                    description = services[index].second
                )
            }
        }
    }
}

@Composable
fun ServiceCard(serviceName: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0F0C0))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Icône ronde à gauche
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF388E3C)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = serviceName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
