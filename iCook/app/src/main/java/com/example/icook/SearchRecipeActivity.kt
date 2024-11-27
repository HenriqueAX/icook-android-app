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

    // Declaração dos componentes da interface gráfica
    private lateinit var recipeListView: ListView // Lista para exibir as receitas
    private lateinit var btnSearch: Button // Botão de busca
    private lateinit var searchField: EditText // Campo de texto para digitação da busca
    private lateinit var adapter: RecipeAdapter // Adaptador para preencher a lista de receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para interagir com o banco de dados
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada com base na pesquisa

    // Método onCreate chamado ao iniciar a Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Inicializa os componentes da interface
        recipeListView = findViewById(R.id.recipeListView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)

        // Inicializa o DatabaseHelper para acessar as receitas do banco de dados
        dbHelper = DatabaseHelper(this)
        allRecipes = dbHelper.getAllRecipes() // Carrega todas as receitas do banco de dados
        filteredRecipes = allRecipes.toList() // Inicializa a lista filtrada com todas as receitas

        // Configura o adaptador para a ListView com as receitas filtradas
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter

        // Configura o clique do botão de busca
        btnSearch.setOnClickListener {
            searchRecipes() // Chama a função de busca ao clicar no botão
        }

        // Configura o TextWatcher para o campo de texto de pesquisa
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            // Atualiza a lista de receitas à medida que o usuário digita
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString()) // Filtra as receitas com base no texto digitado
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Configura o clique do botão para adicionar uma nova receita
        val btnAddRecipe = findViewById<ImageButton>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            // Inicia a Activity para adicionar uma nova receita
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, 100) // Start activity with result
        }
    }

    // Método chamado quando uma nova receita é adicionada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Atualiza a lista de receitas ao adicionar uma nova receita
            allRecipes = dbHelper.getAllRecipes()
            filterRecipes(searchField.text.toString()) // Refaz o filtro para a pesquisa atual
        }
    }

    // Função para realizar a busca de receitas
    private fun searchRecipes() {
        val query = searchField.text.toString() // Obtém o texto digitado no campo de busca
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query) // Passa o termo da pesquisa para a próxima Activity
        startActivity(intent) // Inicia a Activity de resultados de pesquisa
    }

    // Função para filtrar as receitas com base no termo de busca
    private fun filterRecipes(query: String) {
        // Se o campo de busca estiver vazio, exibe todas as receitas
        filteredRecipes = if (query.isEmpty()) {
            allRecipes
        } else {
            // Filtra as receitas pelo nome, ignorando maiúsculas e minúsculas
            allRecipes.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true)
            }
        }
        // Atualiza o adaptador com as receitas filtradas
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }
}
