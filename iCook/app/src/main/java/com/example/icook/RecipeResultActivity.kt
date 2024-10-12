package com.example.icook

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        recipeListView = findViewById(R.id.recipeListView)

        val query = intent.getStringExtra("QUERY") ?: ""
        val allRecipes = loadRecipes() // Carregar suas receitas
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }

        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }

    private fun loadRecipes(): List<Recipe> {
        // Carregue suas receitas aqui (substitua por seu método de carregamento)
        return listOf(
            Recipe("Bolo de Chocolate", "40 min", 5, "Ingredientes do bolo de chocolate", "Modo de preparo do bolo de chocolate"),
            Recipe("Bolo de Cenoura", "50 min", 4, "Ingredientes do bolo de cenoura", "Modo de preparo do bolo de cenoura"),
            Recipe("Bolo de Laranja", "35 min", 4, "Ingredientes do bolo de laranja", "Modo de preparo do bolo de laranja")
        )
    }
}
