package com.example.icook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val recipeName: TextView = findViewById(R.id.recipeName)
        val prepTime: TextView = findViewById(R.id.prepTime)
        val rating: TextView = findViewById(R.id.rating)
        val ingredientsRecyclerView: RecyclerView = findViewById(R.id.ingredientsRecyclerView)
        val instructions: TextView = findViewById(R.id.instructions)
        val recipeImage: ImageView = findViewById(R.id.recipeImage)

        val recipe = intent.getSerializableExtra("RECIPE") as Recipe

        recipeName.text = recipe.name
        prepTime.text = "${recipe.prepTime} min" // Exibe "min" na tela, mas não salva no banco
        rating.text = "${recipe.rating}/5"
        instructions.text = recipe.instructions

        // Defina a imagem da receita
        recipeImage.setImageResource(recipe.imageResId)

        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this)
        ingredientsRecyclerView.adapter = IngredientAdapter(recipe.ingredients.split("\n"))
    }
}
