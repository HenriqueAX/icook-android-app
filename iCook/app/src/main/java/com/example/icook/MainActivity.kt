package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // Declaração das variáveis necessárias para autenticação e interface
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private lateinit var textViewName: TextView
    private lateinit var progressBar: ProgressBar // Spinner de carregamento
    private lateinit var dbHelper: DatabaseHelper // Gerencia o banco de dados local

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuração do Firebase Authentication
        mAuth = FirebaseAuth.getInstance()

        // Configuração das opções de login do Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Token do cliente Web
            .requestEmail() // Solicita o e-mail do usuário
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializa os componentes visuais
        textViewName = findViewById(R.id.name)
        progressBar = findViewById(R.id.progress_bar)
        dbHelper = DatabaseHelper(this)

        // Adiciona receitas padrão ao banco de dados, se necessário
        if (dbHelper.getAllRecipes().isEmpty()) {
            addDefaultRecipes() // Adiciona receitas predefinidas
        }

        // Verifica se há um usuário logado no Firebase
        val user = Firebase.auth.currentUser
        if (user != null) {
            // Exibe o nome do usuário logado
            val userName = user.displayName ?: "Usuário"
            textViewName.text = "Bem-vindo, $userName"
            progressBar.isVisible = true // Exibe o spinner de carregamento

            // Aguarda 3 segundos e redireciona para a tela de busca de receitas
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToSearchRecipeActivity() // Redireciona para a próxima tela
            }, 3000) // Tempo de espera: 3 segundos
        } else {
            textViewName.text = "Faça login para continuar" // Mensagem para usuários não logados
        }
    }

    // Função que adiciona receitas padrão ao banco de dados
    private fun addDefaultRecipes() {
        // Receita 1: Bolo de Chocolate
        val recipe1 = Recipe(
            id = 1,
            name = "Bolo de Chocolate",
            prepTime = 40,
            rating = 5,
            ingredients = "2 xícaras de farinha de trigo\n" +
                    "1 xícara de chocolate em pó\n" +
                    "2 xícaras de açúcar\n" +
                    "1 xícara de leite\n" +
                    "1/2 xícara de óleo\n" +
                    "3 ovos\n" +
                    "1 colher de sopa de fermento em pó",
            instructions = "Primeiro, preaqueça o forno a 180°C. Em uma tigela, misture todos os ingredientes secos...",
            imageResId = R.drawable.bolo_chocolate
        )

        // Receita 2: Bolo de Cenoura
        val recipe2 = Recipe(
            id = 2,
            name = "Bolo de Cenoura",
            prepTime = 50,
            rating = 4,
            ingredients = "2 xícaras de cenoura ralada\n" +
                    "1 xícara de óleo\n" +
                    "2 xícaras de açúcar\n" +
                    "3 ovos\n" +
                    "2 xícaras de farinha de trigo\n" +
                    "1 colher de sopa de fermento em pó",
            instructions = "Primeiro, preaqueça o forno a 180°C. No liquidificador, bata a cenoura, o óleo...",
            imageResId = R.drawable.bolo_cenoura
        )

        // Receita 3: Bolo de Laranja
        val recipe3 = Recipe(
            id = 3,
            name = "Bolo de Laranja",
            prepTime = 35,
            rating = 4,
            ingredients = "2 xícaras de farinha de trigo\n" +
                    "1 xícara de açúcar\n" +
                    "1/2 xícara de óleo\n" +
                    "1 xícara de suco de laranja\n" +
                    "3 ovos\n" +
                    "1 colher de sopa de fermento em pó\n" +
                    "Raspas de 1 laranja",
            instructions = "Primeiro, preaqueça o forno a 180°C. Em uma tigela, misture todos os ingredientes...",
            imageResId = R.drawable.bolo_laranja
        )

        // Adiciona as receitas ao banco de dados
        dbHelper.addRecipe(recipe1)
        dbHelper.addRecipe(recipe2)
        dbHelper.addRecipe(recipe3)
    }

    // Função para redirecionar para a tela de busca de receitas
    private fun navigateToSearchRecipeActivity() {
        val intent = Intent(this, SearchRecipeActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a `MainActivity` para evitar que o usuário volte para ela
    }
}
