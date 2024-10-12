package com.example.icook

import java.io.Serializable

data class Recipe(
    val name: String,
    val prepTime: String,
    val rating: Int,
    val ingredients: String,
    val instructions: String
) : Serializable
