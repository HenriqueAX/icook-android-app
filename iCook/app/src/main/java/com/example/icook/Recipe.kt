package com.example.icook

import java.io.Serializable

data class Recipe(
    val name: String,
    val prepTime: String,
    val rating: Int,
    val ingredients: String, // Exemplo: "Farinha, Açúcar, Ovos"
    val instructions: String
) : Serializable

