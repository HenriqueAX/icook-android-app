package com.example.icook

import java.io.Serializable

// Data class para representar uma receita
data class Recipe(
    val id: Int, // ID único da receita (gerado automaticamente pelo banco de dados)
    val name: String, // Nome da receita
    val prepTime: Int, // Tempo de preparo da receita, em minutos
    val rating: Int, // Avaliação da receita (de 1 a 5)
    val ingredients: String, // Ingredientes necessários para a receita
    val instructions: String, // Instruções de preparo da receita
    val imageResId: Int? = null, // ID do recurso da imagem (opcional), usado quando a imagem é um recurso do app
    val imageUri: String? = null // URI da imagem (opcional), usado quando a imagem é carregada localmente pelo usuário
) : Serializable // A classe implementa Serializable para ser facilmente passada entre atividades
