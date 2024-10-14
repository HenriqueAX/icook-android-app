package com.example.icook

import java.io.Serializable

data class Recipe(
    val id: Int,
    val name: String,
    val prepTime: Int,
    val rating: Int,
    val ingredients: String,
    val instructions: String,
    val imageUri: String? = null // Modificado para armazenar a URI da imagem
) : Serializable
