package com.example.icook

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var etRecipeName: EditText
    private lateinit var etPrepTime: EditText
    private lateinit var etRating: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var btnAdd: Button
    private lateinit var imgRecipe: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        etRecipeName = findViewById(R.id.etRecipeName)
        etPrepTime = findViewById(R.id.etPrepTime)
        etRating = findViewById(R.id.etRating)
        etIngredients = findViewById(R.id.etIngredients)
        etInstructions = findViewById(R.id.etInstructions)
        btnAdd = findViewById(R.id.btnAdd)
        imgRecipe = findViewById(R.id.imgRecipe)

        dbHelper = DatabaseHelper(this)

        imgRecipe.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 100)
        }

        btnAdd.setOnClickListener {
            addRecipe()
        }
    }

    private fun addRecipe() {
        val name = etRecipeName.text.toString()
        val prepTime = etPrepTime.text.toString().toIntOrNull() ?: 0
        val rating = etRating.text.toString().toIntOrNull() ?: 0
        val ingredients = etIngredients.text.toString()
        val instructions = etInstructions.text.toString()

        if (name.isNotEmpty() && prepTime > 0 && rating in 1..5) {
            val savedImagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }
            val recipe = Recipe(
                id = 0,
                name = name,
                prepTime = prepTime,
                rating = rating,
                ingredients = ingredients,
                instructions = instructions,
                imageUri = savedImagePath // Salve o caminho interno da imagem
            )
            dbHelper.addRecipe(recipe)
            Toast.makeText(this, "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(
                this,
                "Por favor, preencha todos os campos corretamente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            imgRecipe.setImageURI(selectedImageUri)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val fileName = "recipe_image_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return file.absolutePath
    }
}
