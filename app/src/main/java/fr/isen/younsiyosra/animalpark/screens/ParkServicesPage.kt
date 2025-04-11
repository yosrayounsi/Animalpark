package fr.isen.younsiyosra.animalpark.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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

    val descriptions = listOf(
        "Accès à des sanitaires propres et accessibles",
        "Zones pour remplir vos gourdes ou bouteilles",
        "Souvenirs, peluches et produits locaux",
        "Point d'arrivée et de départ des visiteurs",
        "Trajet en train pour explorer le parc",
        "Hébergements confortables au cœur du zoo",
        "Ateliers éducatifs pour enfants et familles",
        "Coin ombragé avec ambiance tropicale",
        "Café mobile avec boissons fraîches",
        "Pause gourmande dans un cadre naturel",
        "Jeux pour enfants et familles",
        "Zone de détente avec tables et bancs",
        "Vue panoramique sur les animaux"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Park Services") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF4E7936),
                titleContentColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(services) { index, service ->
                ParkServiceItem(serviceName = service, description = descriptions[index])
            }
        }
    }
}

@Composable
fun ParkServiceItem(serviceName: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F3EA))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = serviceName,
                style = TextStyle(fontSize = 18.sp, color = Color(0xFF2E4F28))
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
            )
        }
    }
}
