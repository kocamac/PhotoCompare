package com.example.uygulamaadi

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.text
import androidx.core.graphics.drawable.toBitmap

class MainActivity : AppCompatActivity() {

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var analyzeButton: Button
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        analyzeButton = findViewById(R.id.analyzeButton)
        textViewResult = findViewById(R.id.textViewResult)

        analyzeButton.setOnClickListener {
            // Resimleri imageView1 ve imageView2'den alın
            val image1 = imageView1.drawable.toBitmap() // toBitmap() fonksiyonunu eklemeniz gerekebilir
            val image2 = imageView2.drawable.toBitmap() // toBitmap() fonksiyonunu eklemeniz gerekebilir

            val result = compareImages(image1, image2)
            textViewResult.text = result
        }
    }

    private fun compareImages(image1: Bitmap, image2: Bitmap): String {
        // Basit bir karşılaştırma: Nesnenin kapladığı piksel sayısını karşılaştırın
        val objectSize1 = getObjectSize(image1)
        val objectSize2 = getObjectSize(image2)

        return if (objectSize2 < objectSize1) {
            "Nesne uzaklaşıyor"
        } else {
            "Nesne yaklaşıyor veya aynı mesafede"
        }
    }

    private fun getObjectSize(image: Bitmap): Int {
        // Nesnenin kapladığı piksel sayısını hesaplayın (basitleştirilmiş örnek)
        var objectSize = 0
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                // Nesne piksellerini belirlemek için renk veya diğer özellikler kullanılabilir
                if (image.getPixel(x, y) == Color.RED) {
                    objectSize++
                }
            }
        }
        return objectSize
    }
}