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

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private lateinit var textViewName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o FirebaseAuth e o cliente de login do Google
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Liga componentes da interface às variáveis
        textViewName = findViewById(R.id.name)
        progressBar = findViewById(R.id.progress_bar)
        dbHelper = DatabaseHelper(this)

        // Adiciona receitas padrão ao banco caso ainda não existam
        if (dbHelper.getAllRecipes().isEmpty()) {
            addDefaultRecipes()
        }

        // Exibe o nome do usuário recebido da intent e redireciona para a próxima tela
        val userName = intent.getStringExtra("USER_NAME")
        if (userName != null) {
            textViewName.text = "Bem-vindo, $userName"
            progressBar.isVisible = true

            // Aguarda 3 segundos e navega para a próxima tela
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToSearchRecipeActivity(userName)
            }, 3000)
        } else {
            textViewName.text = "Erro ao carregar o usuário" // Exibe mensagem de erro caso o nome seja nulo
        }
    }

    private fun addDefaultRecipes() {
        // Função responsável por adicionar receitas padrão ao banco de dados
    }

    private fun navigateToSearchRecipeActivity(userName: String) {
        // Redireciona para a tela de busca de receitas com o nome do usuário
        val intent = Intent(this, SearchRecipeActivity::class.java)
        intent.putExtra("USER_NAME", userName) // Passa o nome do usuário para a próxima Activity
        startActivity(intent)
        finish()
    }
}
