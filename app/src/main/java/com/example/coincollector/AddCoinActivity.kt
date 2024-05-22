package com.example.coincollector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.room.Room
import java.io.File

class AddCoinActivity : AppCompatActivity() {

    private lateinit var yearEditText: EditText
    private lateinit var raritySpinner: Spinner
    private lateinit var quantityEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var captureImageButton: Button
    private lateinit var coinImageView: ImageView
    private var currentPhotoPath: String? = null

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            dispatchTakePictureIntent()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            coinImageView.setImageBitmap(bitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coin)

        yearEditText = findViewById(R.id.yearEditText)
        raritySpinner = findViewById(R.id.raritySpinner)
        quantityEditText = findViewById(R.id.quantityEditText)
        valueEditText = findViewById(R.id.valueEditText)
        saveButton = findViewById(R.id.saveButton)
        captureImageButton = findViewById(R.id.captureImageButton)
        coinImageView = findViewById(R.id.coinImageView)

        captureImageButton.setOnClickListener {
            checkCameraPermissionAndCaptureImage()
        }

        saveButton.setOnClickListener {
            saveCoin()
        }
    }

    private fun checkCameraPermissionAndCaptureImage() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: Exception) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show()
            null
        }
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.coincollector.fileprovider",
                it
            )
            takePictureLauncher.launch(photoURI)
        }
    }

    private fun createImageFile(): File {
        val storageDir: File = getExternalFilesDir(null)!!
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun saveCoin() {
        val year = yearEditText.text.toString()
        val rarity = raritySpinner.selectedItem.toString()
        val quantity = quantityEditText.text.toString().toIntOrNull()
        val value = valueEditText.text.toString().toDoubleOrNull()

        if (year.isEmpty() || rarity.isEmpty() || quantity == null || value == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val coin = Coin(0, year, rarity, quantity, value, currentPhotoPath ?: "")

        val db = Room.databaseBuilder(
            applicationContext,
            CoinDatabase::class.java, "coins.db"
        ).allowMainThreadQueries().build()

        db.coinDao().insert(coin)
        finish()
    }
}
