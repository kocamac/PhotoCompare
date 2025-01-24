package com.example.uygulamaadi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var captureButton1: Button
    private lateinit var captureButton2: Button
    private lateinit var analyzeButton: Button
    private lateinit var resultText: TextView
    
    private var firstImage: Bitmap? = null
    private var secondImage: Bitmap? = null
    
    private val CAMERA_PERMISSION_CODE = 101
    private val CAMERA_REQUEST_CODE1 = 102
    private val CAMERA_REQUEST_CODE2 = 103

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI bileşenlerini başlat
        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        captureButton1 = findViewById(R.id.captureButton1)
        captureButton2 = findViewById(R.id.captureButton2)
        analyzeButton = findViewById(R.id.analyzeButton)
        resultText = findViewById(R.id.resultText)

        // Buton tıklama olaylarını ayarla
        captureButton1.setOnClickListener {
            if (checkCameraPermission()) {
                captureImage(CAMERA_REQUEST_CODE1)
            }
        }

        captureButton2.setOnClickListener {
            if (checkCameraPermission()) {
                captureImage(CAMERA_REQUEST_CODE2)
            }
        }

        analyzeButton.setOnClickListener {
            analyzeImages()
        }
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
            return false
        }
        return true
    }

    private fun captureImage(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            when (requestCode) {
                CAMERA_REQUEST_CODE1 -> {
                    imageView1.setImageBitmap(imageBitmap)
                    firstImage = imageBitmap
                }
                CAMERA_REQUEST_CODE2 -> {
                    imageView2.setImageBitmap(imageBitmap)
                    secondImage = imageBitmap
                }
            }
        }
    }

    private fun analyzeImages() {
        if (firstImage == null || secondImage == null) {
            resultText.text = "Lütfen önce iki fotoğraf çekin"
            return
        }

        // Basit bir boyut karşılaştırması
        val firstImageSize = firstImage!!.width * firstImage!!.height
        val secondImageSize = secondImage!!.width * secondImage!!.height

        val difference = ((secondImageSize - firstImageSize).toFloat() / firstImageSize) * 100

        val result = when {
            difference > 10 -> "Nesne yaklaşmış görünüyor (${String.format("%.2f", difference)}% değişim)"
            difference < -10 -> "Nesne uzaklaşmış görünüyor (${String.format("%.2f", -difference)}% değişim)"
            else -> "Nesnenin konumu önemli bir değişiklik göstermiyor"
        }

        resultText.text = result
    }
}