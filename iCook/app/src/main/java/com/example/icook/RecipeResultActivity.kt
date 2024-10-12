package com.example.icook

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        recipeListView = findViewById(R.id.recipeListView)

        // Exemplo de dados - Adicionando o valor para o parâmetro 'rating'
        val recipes = listOf(
            Recipe("Bolo de Chocolate", "40 min", 5),
            Recipe("Bolo de Cenoura", "50 min", 4),
            Recipe("Bolo de Laranja", "35 min", 4)
        )

        val adapter = RecipeAdapter(this, recipes)
        recipeListView.adapter = adapter
    }
}
