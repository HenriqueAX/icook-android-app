package com.example.icook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Activity que exibe os resultados da busca de receitas
class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView // RecyclerView para exibir receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para acessar o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        // Inicializa o RecyclerView a partir do layout
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)

        // Obtém a consulta de busca passada pela Activity anterior
        val query = intent.getStringExtra("QUERY") ?: ""

        // Inicializa o helper do banco de dados e carrega todas as receitas
        dbHelper = DatabaseHelper(this)
        val allRecipes = dbHelper.getAllRecipes()

        // Filtra as receitas com base na consulta de busca
        val filteredRecipes = allRecipes.filter { it.name.contains(query, ignoreCase = true) }

        // Configura o RecyclerView com um layout linear e o adaptador personalizado
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.adapter = RecipeAdapter(this, filteredRecipes) { recipe ->
            // Navega para a tela de detalhes da receita selecionada
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            startActivity(intent)
        }
    }
}
