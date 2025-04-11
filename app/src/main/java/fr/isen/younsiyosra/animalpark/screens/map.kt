package fr.isen.younsiyosra.animalpark.screens

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.isen.younsiyosra.animalpark.R

// Données pour chaque enclos
data class Enclosure(
    val name: String,
    val color: String,
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val isOpen: Boolean,
    val imageResId: Int
)

class Map(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
        textSize = 30f
        color = Color.BLACK
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private val enclosures = listOf(
        Enclosure("La Bergerie des reptiles", "#70D5C2", 300f, 300f, 150f, true, R.drawable.reptile_image),
        Enclosure("Le Vallon des cascades", "#A4BDCC", 900f, 300f, 150f, false, R.drawable.waterfall_image),
        Enclosure("Le Belvédère", "#B5A589", 300f, 700f, 150f, true, R.drawable.belvedere_image),
        Enclosure("Le Plateau", "#E2A59D", 900f, 700f, 150f, false, R.drawable.plateau_image),
        Enclosure("Les Clairières", "#E2CA9D", 300f, 1100f, 150f, true, R.drawable.clairiere_image),
        Enclosure("Le Bois des pins", "#C5E29D", 900f, 1100f, 150f, true, R.drawable.pins_image)
    )

    private var backgroundBitmap: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.background_animalpark)

    private var selectedImage: Bitmap? = null
    private var closeButtonBounds: RectF? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scaledBg = Bitmap.createScaledBitmap(backgroundBitmap, width, height, true)
        canvas.drawBitmap(scaledBg, 0f, 0f, null)

        selectedImage?.let {
            // Affiche l'image sélectionnée
            canvas.drawBitmap(it, width / 2f - it.width / 2f, height / 2f - it.height / 2f, null)

            // Dessine le bouton de retour
            val buttonPaint = Paint().apply {
                color = Color.argb(200, 0, 0, 0)
                isAntiAlias = true
            }
            val buttonTextPaint = Paint().apply {
                color = Color.WHITE
                textSize = 36f
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }

            val buttonWidth = 300f
            val buttonHeight = 100f
            val buttonLeft = width / 2f - buttonWidth / 2f
            val buttonTop = height - 200f
            val buttonRight = buttonLeft + buttonWidth
            val buttonBottom = buttonTop + buttonHeight

            canvas.drawRoundRect(
                RectF(buttonLeft, buttonTop, buttonRight, buttonBottom),
                20f, 20f, buttonPaint
            )
            canvas.drawText("← Fermer", width / 2f, buttonTop + 65f, buttonTextPaint)

            closeButtonBounds = RectF(buttonLeft, buttonTop, buttonRight, buttonBottom)
        } ?: run {
            // Affiche les enclos si aucune image n’est affichée
            enclosures.forEach { enclosure ->
                val circlePaint = Paint().apply { color = Color.parseColor(enclosure.color) }
                canvas.drawCircle(enclosure.centerX, enclosure.centerY, enclosure.radius, circlePaint)
                canvas.drawText(enclosure.name, enclosure.centerX, enclosure.centerY, textPaint)
                canvas.drawText(
                    if (enclosure.isOpen) "Ouvert" else "Fermé",
                    enclosure.centerX,
                    enclosure.centerY + 40f,
                    textPaint
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y

            // Si image affichée, détecter clic sur bouton "Fermer"
            selectedImage?.let {
                closeButtonBounds?.let { bounds ->
                    if (bounds.contains(x, y)) {
                        selectedImage = null
                        closeButtonBounds = null
                        invalidate()
                        return true
                    }
                }
            } ?: run {
                // Sinon détecter si clic sur un enclos
                enclosures.forEach { enclosure ->
                    val distance = Math.hypot(
                        (x - enclosure.centerX).toDouble(),
                        (y - enclosure.centerY).toDouble()
                    )
                    if (distance < enclosure.radius) {
                        selectedImage = BitmapFactory.decodeResource(context.resources, enclosure.imageResId)
                        invalidate()
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
