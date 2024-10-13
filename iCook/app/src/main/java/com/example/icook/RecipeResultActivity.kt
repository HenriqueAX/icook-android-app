package com.example.icook

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView
    private lateinit var adapter: RecipeAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        recipeListView = findViewById(R.id.recipeListView)

        val query = intent.getStringExtra("QUERY") ?: ""

        // Inicializa o DatabaseHelper
        dbHelper = DatabaseHelper(this)
        val allRecipes = dbHelper.getAllRecipes() // Carrega as receitas do banco de dados
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }

        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }
}
