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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

// Activity responsável pela busca de receitas
class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var recipeRecyclerView: RecyclerView // RecyclerView para receitas
    private lateinit var btnSearch: Button // Botão de busca
    private lateinit var searchField: EditText // Campo de texto para busca
    private lateinit var dbHelper: DatabaseHelper // Helper para acessar o banco de dados
    private lateinit var recipeAdapter: RecipeAdapter // Adaptador do RecyclerView
    private lateinit var mGoogleSignInClient: GoogleSignInClient // Cliente de login do Google
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada de receitas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        // Inicializa os componentes
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)
        val btnLogout = findViewById<Button>(R.id.logout_button)

        // Inicializa o banco de dados e receitas
        dbHelper = DatabaseHelper(this)
        allRecipes = dbHelper.getAllRecipes()
        filteredRecipes = allRecipes

        // Configura o RecyclerView
        setupRecyclerView()

        // Configura o cliente de login do Google
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

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

        // Botão para adicionar uma nova receita
        val btnAddRecipe = findViewById<ImageButton>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivityForResult(intent, 100)
        }

        // Botão de logout
        btnLogout.setOnClickListener {
            logout()
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

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            allRecipes = dbHelper.getAllRecipes()
            filterRecipes(searchField.text.toString())
        }
    }
}
