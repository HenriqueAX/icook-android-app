package com.example.icook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val recipeName: TextView = findViewById(R.id.recipeName)
        val prepTime: TextView = findViewById(R.id.prepTime)
        val ingredients: TextView = findViewById(R.id.ingredients)
        val instructions: TextView = findViewById(R.id.instructions)
        val recipeImage: ImageView = findViewById(R.id.recipeImage)

        val recipe = intent.getSerializableExtra("RECIPE") as Recipe

        recipeName.text = recipe.name
        prepTime.text = recipe.prepTime
        ingredients.text = recipe.ingredients
        instructions.text = recipe.instructions

        // Defina a imagem da receita
        when (recipe.name) {
            "Bolo de Chocolate" -> recipeImage.setImageResource(R.drawable.bolo_chocolate)
            "Bolo de Cenoura" -> recipeImage.setImageResource(R.drawable.bolo_cenoura)
            "Bolo de Laranja" -> recipeImage.setImageResource(R.drawable.bolo_laranja)
            else -> recipeImage.setImageResource(R.drawable.recipe_placeholder)
        }
    }
}
