package com.example.coincollector

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.room.Room
import java.io.File

class AddCoinActivity : AppCompatActivity() {

    private lateinit var yearEditText: EditText
    private lateinit var rarityEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var captureImageButton: Button
    private lateinit var coinImageView: ImageView
    private var currentPhotoPath: String? = null // Rendre nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coin)

        yearEditText = findViewById(R.id.yearEditText)
        rarityEditText = findViewById(R.id.rarityEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        valueEditText = findViewById(R.id.valueEditText)
        saveButton = findViewById(R.id.saveButton)
        captureImageButton = findViewById(R.id.captureImageButton)
        coinImageView = findViewById(R.id.coinImageView)

        captureImageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        saveButton.setOnClickListener {
            saveCoin()
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: Exception) {
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.coincollector.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            coinImageView.setImageBitmap(bitmap)
        }
    }

    private fun saveCoin() {
        val year = yearEditText.text.toString()
        val rarity = rarityEditText.text.toString()
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
        Log.d("AddCoinActivity", "Coin inserted: $coin")
        finish()
    }
}
