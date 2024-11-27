package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Activity responsável pelo login do usuário
class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText         // Campo para nome de usuário
    private lateinit var etPassword: EditText         // Campo para senha
    private lateinit var btnLogin: Button             // Botão de login
    private lateinit var dbHelper: DatabaseHelper     // Helper para interagir com o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialização dos componentes da interface com base no layout XML
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Inicializa o banco de dados
        dbHelper = DatabaseHelper(this)

        // Adiciona receitas padrão se o banco de dados estiver vazio
        if (dbHelper.getAllRecipes().isEmpty()) {
            addDefaultRecipes() // Função que adiciona receitas iniciais ao banco
        }

        // Configura o evento de clique no botão de login
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Valida as credenciais de login
            if (username == "admin" && password == "1234") {
                val intent = Intent(this, SearchRecipeActivity::class.java)
                startActivity(intent) // Navega para a tela de busca de receitas
            } else {
                Toast.makeText(this, "Usuário e senha incorretos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Adiciona receitas padrão ao banco de dados
    private fun addDefaultRecipes() {
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
}
