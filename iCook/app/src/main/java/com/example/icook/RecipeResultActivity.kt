package com.example.icook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton

// Activity que exibe os resultados da busca de receitas
class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView // RecyclerView para exibir receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para acessar o banco de dados
    private lateinit var btnSearchRecipes: ImageButton // Botão para buscar receitas
    private lateinit var btnAddRecipe: ImageButton // Botão para adicionar uma nova receita

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        // Inicializa os componentes visuais do layout
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        btnSearchRecipes = findViewById(R.id.btnSearchRecipes)
        btnAddRecipe = findViewById(R.id.btnAddRecipe)

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

        // Configura o clique no botão de buscar receitas para abrir a Activity de busca
        btnSearchRecipes.setOnClickListener {
            val intent = Intent(this, SearchRecipeActivity::class.java)
            startActivity(intent) // Inicia a Activity de busca de receitas
        }

        // Configura o clique no botão de adicionar receita para abrir a Activity de adicionar
        btnAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, ADD_RECIPE_REQUEST_CODE) // Inicia a Activity para adicionar receitas
        }
    }

    // Código para atualizar a lista de receitas após adicionar uma nova receita
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_RECIPE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Recarrega todas as receitas do banco de dados
            val allRecipes = dbHelper.getAllRecipes()
            val query = intent.getStringExtra("QUERY") ?: ""
            val filteredRecipes = allRecipes.filter { it.name.contains(query, ignoreCase = true) }

            // Atualiza o adaptador com as receitas filtradas
            (recipeRecyclerView.adapter as RecipeAdapter).updateRecipes(filteredRecipes)
        }
    }

    companion object {
        private const val ADD_RECIPE_REQUEST_CODE = 100 // Código de requisição para adicionar receita
    }
}
