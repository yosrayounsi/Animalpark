package fr.isen.younsiyosra.animalpark.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.younsiyosra.animalpark.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkServicesScreen(modifier: Modifier = Modifier) {
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Services du Parc") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF4E7936),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 🌿 Image de fond douce
            Image(
                painter = painterResource(id = R.drawable.background_animalpark),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.Crop
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { service ->
                    ParkServiceItem(serviceName = service)
                }
            }
        }
    }
}

@Composable
fun String.ParkServiceItem() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F8E9)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Icône",
                tint = Color(0xFF4E7936),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = this@ParkServiceItem,
                style = TextStyle(fontSize = 18.sp, color = Color(0xFF2E2E2E))
            )
        }
    }
}
