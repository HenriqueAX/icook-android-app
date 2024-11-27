package com.example.icook

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

// Activity responsável pela busca de receitas
class SearchRecipeActivity : AppCompatActivity() {

    // Declaração das variáveis de interface e autenticação
    private lateinit var recipeListView: ListView // Lista de receitas
    private lateinit var btnSearch: Button // Botão de busca
    private lateinit var searchField: EditText // Campo de texto para busca
    private lateinit var adapter: RecipeAdapter // Adaptador para a lista de receitas
    private lateinit var dbHelper: DatabaseHelper // Helper para interagir com o banco de dados
    private lateinit var mGoogleSignInClient: GoogleSignInClient // Cliente do Google Sign-In
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada com base na pesquisa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe) // Define o layout da Activity

        // Inicializa os componentes da interface
        recipeListView = findViewById(R.id.recipeListView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)
        val btnLogout = findViewById<Button>(R.id.logout_button) // Botão de logout

        // Inicializa o helper do banco de dados
        dbHelper = DatabaseHelper(this)

        // Configura o cliente de login do Google
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        // Carrega todas as receitas do banco de dados
        allRecipes = dbHelper.getAllRecipes()
        filteredRecipes = allRecipes.toList() // Inicializa com todas as receitas

        // Configura o adaptador para a ListView com a lista completa de receitas
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter

        // Define a ação para o botão de busca
        btnSearch.setOnClickListener {
            searchRecipes() // Realiza a busca com base no texto digitado
        }

        // Adiciona um listener para monitorar mudanças no campo de busca
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Não utilizado
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString()) // Filtra as receitas conforme o texto digitado
            }

            override fun afterTextChanged(s: Editable?) {
                // Não utilizado
            }
        })

        // Configura o botão para adicionar uma nova receita
        val btnAddRecipe = findViewById<ImageButton>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            // Inicia a Activity para adicionar uma nova receita
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, 100) // Chama a Activity com resultado
        }

        // Configura o botão de logout
        btnLogout.setOnClickListener {
            logout() // Executa a função de logout
        }
    }

    // Função que realiza o logout do usuário
    private fun logout() {
        // Faz logout do Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Faz logout do Google Sign-In
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Redireciona para a tela de login após o logout
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a Activity atual para evitar que o usuário volte
        }
    }

    // Método chamado quando uma Activity retorna um resultado
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            allRecipes = dbHelper.getAllRecipes() // Atualiza todas as receitas do banco de dados
            filterRecipes(searchField.text.toString()) // Refaz o filtro com a pesquisa atual
        }
    }

    // Função para iniciar a busca de receitas e redirecionar para a Activity de resultados
    private fun searchRecipes() {
        val query = searchField.text.toString() // Obtém o termo da busca
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query) // Passa a busca para a próxima Activity
        startActivity(intent) // Inicia a Activity de resultados
    }

    // Função para filtrar as receitas com base no termo de busca
    private fun filterRecipes(query: String) {
        // Filtra as receitas pelo nome, ignorando maiúsculas e minúsculas
        filteredRecipes = if (query.isEmpty()) {
            allRecipes // Se não houver texto, exibe todas as receitas
        } else {
            allRecipes.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true)
            }
        }
        // Atualiza o adaptador com as receitas filtradas
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }
}
