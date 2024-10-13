package com.example.icook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var etRecipeName: EditText
    private lateinit var etPrepTime: EditText
    private lateinit var etRating: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var btnAdd: Button
    private lateinit var imgRecipe: ImageView // Campo para exibir a imagem
    private var selectedImageUri: Uri? = null // Para armazenar a URI da imagem selecionada
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
        imgRecipe = findViewById(R.id.imgRecipe) // Inicializa o ImageView

        dbHelper = DatabaseHelper(this)

        imgRecipe.setOnClickListener {
            // Abre um seletor de imagens
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, 100)
        }

        btnAdd.setOnClickListener {
            addRecipe()
        }

        findViewById<ImageButton>(R.id.btnSearchRecipes).setOnClickListener {
            val intent = Intent(this, SearchRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addRecipe() {
        val name = etRecipeName.text.toString()
        val prepTime = etPrepTime.text.toString().toIntOrNull() ?: 0 // Agora obtém o valor como número simples
        val rating = etRating.text.toString().toIntOrNull() ?: 0
        val ingredients = etIngredients.text.toString()
        val instructions = etInstructions.text.toString()

        if (name.isNotEmpty() && prepTime > 0 && rating in 1..5) {
            val recipe = Recipe(
                id = 0,
                name = name,
                prepTime = prepTime,
                rating = rating,
                ingredients = ingredients,
                instructions = instructions,
                imageResId = R.drawable.recipe_placeholder // Defina uma imagem padrão ou ajuste conforme necessário
            )
            dbHelper.addRecipe(recipe)
            Toast.makeText(this, "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()
            finish() // Retorna para a tela anterior
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            imgRecipe.setImageURI(selectedImageUri) // Exibe a imagem selecionada
        }
    }
}
