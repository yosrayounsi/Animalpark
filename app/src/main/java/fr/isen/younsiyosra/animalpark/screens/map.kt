package fr.isen.younsiyosra.animalpark.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import fr.isen.younsiyosra.animalpark.R

// Classe pour représenter un enclos avec une couleur, une position et un état (ouvert ou fermé)
data class Enclosure(val name: String, val color: String, val centerX: Float, val centerY: Float, val radius: Float, val isOpen: Boolean)

class Map(context: Context) : View(context) {

    private val paint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        textSize = 30f
        style = Paint.Style.FILL
    }

    private val textPaint: Paint = Paint().apply {
        color = Color.WHITE
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    private lateinit var backgroundBitmap: Bitmap

    // Liste des enclos avec leurs couleurs, positions, rayons et état (ouvert ou fermé)
    private val enclosures = listOf(
        Enclosure("La Bergerie des reptiles", "#70D5C2", 300f, 300f, 150f, true),
        Enclosure("Le Vallon des cascades", "#A4BDCC", 900f, 300f, 150f, false),
        Enclosure("Le Belvédère", "#B5A589", 300f, 700f, 150f, true),
        Enclosure("Le Plateau", "#E2A59D", 900f, 700f, 150f, false),
        Enclosure("Les Clairières", "#E2CA9D", 300f, 1100f, 150f, true),
        Enclosure("Le Bois des pins", "#C5E29D", 900f, 1100f, 150f, true)
    )

    init {
        // Charger l'image de fond
        backgroundBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mapzoo)  // Remplace par ton image
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Récupérer la largeur et la hauteur de la vue
        val viewWidth = width
        val viewHeight = height

        // Redimensionner l'image pour qu'elle couvre toute la vue
        val scaledBitmap = Bitmap.createScaledBitmap(backgroundBitmap, viewWidth, viewHeight, true)

        // Dessiner l'image de fond redimensionnée
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)

        // Dessiner les enclos sous forme de cercles avec leurs couleurs respectives
        drawEnclosures(canvas)

        // Dessiner les noms des enclos et leur état (ouvert ou fermé)
        drawEnclosureNames(canvas)
    }

    private fun drawEnclosures(canvas: Canvas) {
        // Dessiner les enclos sous forme de cercles avec des couleurs spécifiques
        enclosures.forEach { enclosure ->
            val enclosurePaint = Paint().apply { color = Color.parseColor(enclosure.color) }
            // Dessiner le cercle
            canvas.drawCircle(enclosure.centerX, enclosure.centerY, enclosure.radius, enclosurePaint)
        }
    }

    private fun drawEnclosureNames(canvas: Canvas) {
        // Dessiner les noms des enclos et leur état
        enclosures.forEach { enclosure ->
            canvas.drawText(enclosure.name, enclosure.centerX, enclosure.centerY, textPaint)
            // Afficher l'état de l'enclos (ouvert ou fermé)
            val stateText = if (enclosure.isOpen) "Ouvert" else "Fermé"
            canvas.drawText(stateText, enclosure.centerX, enclosure.centerY + 40f, textPaint)
        }
    }
}
