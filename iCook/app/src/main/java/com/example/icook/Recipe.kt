package com.example.icook

import java.io.Serializable

data class Recipe(
    val id: Int,                // Adiciona o ID
    val name: String,
    val prepTime: Int,       // Agora armazena apenas o número
    val rating: Int,
    val ingredients: String,    // Exemplo: "Farinha, Açúcar, Ovos"
    val instructions: String,
    val imageResId: Int        // Adiciona o recurso de imagem
) : Serializable
