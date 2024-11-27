package com.example.icook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Activity que exibe os resultados da busca de receitas
class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView // RecyclerView para receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para acessar o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        // Inicializa o RecyclerView
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)

        // Obtém a consulta passada pela busca
        val query = intent.getStringExtra("QUERY") ?: ""

        // Inicializa o banco de dados e filtra as receitas
        dbHelper = DatabaseHelper(this)
        val allRecipes = dbHelper.getAllRecipes()
        val filteredRecipes = allRecipes.filter { it.name.contains(query, ignoreCase = true) }

        // Configura o RecyclerView
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.adapter = RecipeAdapter(this, filteredRecipes) { recipe ->
            // Abre a tela de detalhes da receita
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            startActivity(intent)
        }
    }
}
