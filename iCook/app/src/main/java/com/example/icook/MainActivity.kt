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

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        textViewName = findViewById(R.id.name)
        progressBar = findViewById(R.id.progress_bar)
        dbHelper = DatabaseHelper(this)

        if (dbHelper.getAllRecipes().isEmpty()) {
            addDefaultRecipes()
        }

        val userName = intent.getStringExtra("USER_NAME")
        if (userName != null) {
            textViewName.text = "Bem-vindo, $userName"
            progressBar.isVisible = true

            Handler(Looper.getMainLooper()).postDelayed({
                navigateToSearchRecipeActivity()
            }, 3000)
        } else {
            textViewName.text = "Faça login para continuar"
        }
    }

    private fun addDefaultRecipes() {
        // Adiciona receitas padrão (código original)
    }

    private fun navigateToSearchRecipeActivity() {
        val intent = Intent(this, SearchRecipeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
