package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

// Activity responsável pela busca de receitas
class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView // RecyclerView para receitas
    private lateinit var btnSearch: Button // Botão de busca
    private lateinit var searchField: EditText // Campo de texto para busca
    private lateinit var userNameTextView: TextView // TextView para o nome do usuário
    private lateinit var dbHelper: DatabaseHelper // Helper para acessar o banco de dados
    private lateinit var recipeAdapter: RecipeAdapter // Adaptador do RecyclerView
    private lateinit var mGoogleSignInClient: GoogleSignInClient // Cliente de login do Google
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada de receitas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Inicializa os componentes visuais do layout
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)
        userNameTextView = findViewById(R.id.userNameTextView)
        val btnLogout = findViewById<Button>(R.id.logout_button)

        // Recebe o nome do usuário passado pelo Intent
        val userName = intent.getStringExtra("USER_NAME") ?: "Usuário"
        userNameTextView.text = "Bem-vindo, $userName"

        // Carrega todas as receitas do banco de dados
        dbHelper = DatabaseHelper(this)
        allRecipes = dbHelper.getAllRecipes()
        filteredRecipes = allRecipes

        // Configura o RecyclerView para exibir receitas
        setupRecyclerView()

        // Configura o cliente de login do Google para logout
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        // Configura o clique no botão de busca para abrir os resultados em outra Activity
        btnSearch.setOnClickListener {
            searchRecipes()
        }

        // Adiciona um listener ao campo de texto para busca dinâmica
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString()) // Atualiza a lista filtrada em tempo real
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Configura o botão de adicionar nova receita
        val btnAddRecipe = findViewById<ImageButton>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, 100) // Inicia a Activity para adicionar receitas
        }

        // Configura o botão de logout para deslogar o usuário
        btnLogout.setOnClickListener {
            logout()
        }
    }

    // Configura o RecyclerView com layout e adaptador
    private fun setupRecyclerView() {
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter(this, filteredRecipes) { recipe ->
            // Navega para a tela de detalhes da receita ao clicar em um item
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            startActivity(intent)
        }
        recipeRecyclerView.adapter = recipeAdapter
    }

    // Inicia a busca e passa a consulta para outra Activity
    private fun searchRecipes() {
        val query = searchField.text.toString()
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query)
        startActivity(intent)
    }

    // Filtra receitas com base na consulta de texto
    private fun filterRecipes(query: String) {
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

    // Realiza logout do usuário e retorna para a tela de login
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Atualiza a lista de receitas após retornar da tela de adição
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            allRecipes = dbHelper.getAllRecipes()
            filterRecipes(searchField.text.toString())
        }
    }
}
