package com.example.icook

import java.io.Serializable

data class Recipe(
    val id: Int,
    val name: String,
    val prepTime: Int,
    val rating: Int,
    val ingredients: String,
    val instructions: String,
    val imageResId: Int? = null, // Agora é opcional
    val imageUri: String? = null // Para imagens adicionadas localmente
) : Serializable
