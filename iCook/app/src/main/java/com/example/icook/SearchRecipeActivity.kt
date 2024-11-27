package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

// Activity responsável pela busca de receitas
class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView // Lista de receitas
    private lateinit var btnSearch: Button // Botão de busca
    private lateinit var searchField: EditText // Campo de texto para busca
    private lateinit var adapter: RecipeAdapter // Adaptador para a lista de receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para interagir com o banco de dados
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada com base na pesquisa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Inicializa os componentes da interface
        recipeListView = findViewById(R.id.recipeListView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)

        dbHelper = DatabaseHelper(this)
        allRecipes = dbHelper.getAllRecipes() // Carrega todas as receitas
        filteredRecipes = allRecipes.toList() // Inicializa com todas as receitas

        // Configura o adaptador para a ListView
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter

        // Define ação para o botão de busca
        btnSearch.setOnClickListener {
            searchRecipes() // Realiza a busca
        }

        // Configura o TextWatcher para o campo de busca
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString()) // Filtra as receitas conforme o texto digitado
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Configura o botão para adicionar uma nova receita
        val btnAddRecipe = findViewById<ImageButton>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            // Inicia a Activity para adicionar uma nova receita
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, 100) // Chama a Activity com resultado
        }
    }

    // Método para atualizar a lista após adicionar uma receita
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            allRecipes = dbHelper.getAllRecipes() // Atualiza todas as receitas
            filterRecipes(searchField.text.toString()) // Refaz o filtro com a pesquisa atual
        }
    }

    // Função para realizar a busca de receitas
    private fun searchRecipes() {
        val query = searchField.text.toString() // Obtém o termo da busca
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query) // Passa a busca para a próxima Activity
        startActivity(intent) // Inicia a Activity de resultados
    }

    // Função para filtrar as receitas com base no termo de busca
    private fun filterRecipes(query: String) {
        filteredRecipes = if (query.isEmpty()) {
            allRecipes // Se não houver texto, exibe todas as receitas
        } else {
            allRecipes.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true) // Filtra pelo nome da receita
            }
        }
        // Atualiza o adaptador com as receitas filtradas
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }
}
