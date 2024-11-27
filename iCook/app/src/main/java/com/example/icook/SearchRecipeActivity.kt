package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var btnSearch: Button
    private lateinit var searchField: EditText
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recipeAdapter: RecipeAdapter
    private var allRecipes = listOf<Recipe>()
    private var filteredRecipes = listOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Inicializa os componentes do layout
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)

        // Inicializa o banco de dados e carrega receitas
        dbHelper = DatabaseHelper(this)
        loadRecipes()

        // Configura o RecyclerView
        setupRecyclerView()

        // Configura o botão de busca
        btnSearch.setOnClickListener {
            searchRecipes()
        }

        // Adiciona listener ao campo de texto para busca dinâmica
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Configura o botão de adicionar receitas
        val btnAddRecipe = findViewById<ImageButton>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    private fun setupRecyclerView() {
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter(this, filteredRecipes) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            startActivity(intent)
        }
        recipeRecyclerView.adapter = recipeAdapter
    }

    private fun searchRecipes() {
        val query = searchField.text.toString()
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query)
        startActivity(intent)
    }

    private fun filterRecipes(query: String) {
        // Filtra as receitas com base no texto digitado
        filteredRecipes = if (query.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { it.name.contains(query, ignoreCase = true) }
        }
        recipeAdapter = RecipeAdapter(this, filteredRecipes) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            startActivity(intent)
        }
        recipeRecyclerView.adapter = recipeAdapter
    }

    private fun loadRecipes() {
        allRecipes = dbHelper.getAllRecipes()
        filteredRecipes = allRecipes.toList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Atualiza as receitas ao retornar da tela de adição
            loadRecipes()
            filterRecipes(searchField.text.toString())
        }
    }
}
