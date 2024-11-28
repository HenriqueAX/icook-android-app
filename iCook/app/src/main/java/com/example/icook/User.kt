package com.example.icook

// Classe de dados que representa um usuário no sistema
data class User(
    val id: Int = 0,        // ID único do usuário (gerado automaticamente no banco de dados)
    val name: String,       // Nome do usuário
    val email: String,      // E-mail do usuário (deve ser único)
    val password: String    // Senha do usuário
)
