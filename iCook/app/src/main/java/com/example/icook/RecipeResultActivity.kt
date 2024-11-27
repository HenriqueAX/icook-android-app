package com.example.icook

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

// Activity responsável por exibir os resultados da busca de receitas
class RecipeResultActivity : AppCompatActivity() {

    // Declaração dos componentes da interface gráfica
    private lateinit var recipeListView: ListView // Lista de receitas exibidas
    private lateinit var adapter: RecipeAdapter // Adaptador para a lista de receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para interagir com o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        // Inicializa o componente ListView da interface
        recipeListView = findViewById(R.id.recipeListView)

        // Obtém o termo de pesquisa da Intent, caso exista
        val query = intent.getStringExtra("QUERY") ?: "" // Se não houver consulta, usa uma string vazia

        // Inicializa o DatabaseHelper para acessar o banco de dados
        dbHelper = DatabaseHelper(this)

        // Obtém todas as receitas do banco de dados
        val allRecipes = dbHelper.getAllRecipes()

        // Filtra as receitas que contém o texto da consulta no nome (ignora maiúsculas e minúsculas)
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }

        // Inicializa o adaptador com a lista de receitas filtradas
        adapter = RecipeAdapter(this, filteredRecipes)

        // Define o adaptador para o ListView
        recipeListView.adapter = adapter
    }
}
