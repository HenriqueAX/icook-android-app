package com.example.icook

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

// Activity que exibe os resultados da busca de receitas
class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView // Lista de receitas
    private lateinit var adapter: RecipeAdapter // Adaptador para a lista
    private lateinit var dbHelper: DatabaseHelper // Helper para acessar o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        recipeListView = findViewById(R.id.recipeListView) // Inicializa a lista de receitas
        val query = intent.getStringExtra("QUERY") ?: "" // Obtém o termo de busca

        dbHelper = DatabaseHelper(this) // Inicializa o helper para o banco de dados
        val allRecipes = dbHelper.getAllRecipes() // Obtém todas as receitas do banco

        // Filtra as receitas com base na consulta
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }

        // Configura o adaptador com as receitas filtradas
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter // Define o adaptador para o ListView
    }
}
