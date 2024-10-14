package com.example.icook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.FileProvider
import java.io.File

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
        prepTime.text = "${recipe.prepTime} min"
        rating.text = "${recipe.rating}/5"
        instructions.text = recipe.instructions

        // Verifica se a receita tem imageResId ou imageUri para exibir a imagem
        when {
            recipe.imageResId != null -> {
                recipeImage.setImageResource(recipe.imageResId)
            }
            recipe.imageUri != null -> {
                val imageFile = File(recipe.imageUri)
                if (imageFile.exists()) {
                    val imageUri = FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
                    recipeImage.setImageURI(imageUri)
                } else {
                    recipeImage.setImageResource(R.drawable.recipe_placeholder)
                }
            }
            else -> {
                recipeImage.setImageResource(R.drawable.recipe_placeholder)
            }
        }

        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this)
        ingredientsRecyclerView.adapter = IngredientAdapter(recipe.ingredients.split("\n"))
    }
}
