package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView
    private lateinit var btnSearch: Button
    private lateinit var searchField: EditText
    private lateinit var adapter: RecipeAdapter
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada de receitas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        recipeListView = findViewById(R.id.recipeListView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)

        // Carregue suas receitas de algum lugar (por exemplo, de um banco de dados ou lista estática)
        allRecipes = loadRecipes() // Supondo que você tenha uma função que carrega todas as receitas
        filteredRecipes = allRecipes.toList()

        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter

        btnSearch.setOnClickListener {
            searchRecipes()
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchRecipes() {
        val query = searchField.text.toString()
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query)
        startActivity(intent)
    }

    private fun filterRecipes(query: String) {
        filteredRecipes = if (query.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true)
            }
        }
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }

    private fun loadRecipes(): List<Recipe> {
        // Carregue suas receitas aqui (substitua por seu método de carregamento)
        return listOf(
            Recipe("Bolo de Chocolate", "40 min", 5),
            Recipe("Bolo de Cenoura", "50 min", 4),
            Recipe("Bolo de Laranja", "35 min", 4)
        )
    }
}
