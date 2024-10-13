package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var etRecipeName: EditText
    private lateinit var etPrepTime: EditText
    private lateinit var etRating: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var btnAdd: Button
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

        dbHelper = DatabaseHelper(this)

        btnAdd.setOnClickListener {
            addRecipe()
        }

        findViewById<ImageButton>(R.id.btnSearchRecipes).setOnClickListener {
            val intent = Intent(this, SearchRecipeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btnAddRecipe).setOnClickListener {
            // Este botão é redundante, pois estamos na tela de adicionar receita
        }
    }

    private fun addRecipe() {
        val name = etRecipeName.text.toString()
        val prepTime = etPrepTime.text.toString()
        val rating = etRating.text.toString().toIntOrNull() ?: 0
        val ingredients = etIngredients.text.toString()
        val instructions = etInstructions.text.toString()

        if (name.isNotEmpty() && prepTime.isNotEmpty() && rating in 1..5) {
            val recipe = Recipe(
                id = 0, // O ID será gerado automaticamente pelo banco de dados
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
}
