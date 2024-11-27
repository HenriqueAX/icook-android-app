package com.example.icook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.FileProvider
import java.io.File

// Activity responsável por exibir os detalhes de uma receita
class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Inicialização dos componentes da interface gráfica
        val recipeName: TextView = findViewById(R.id.recipeName) // Exibe o nome da receita
        val prepTime: TextView = findViewById(R.id.prepTime) // Exibe o tempo de preparo
        val rating: TextView = findViewById(R.id.rating) // Exibe a avaliação da receita
        val ingredientsRecyclerView: RecyclerView = findViewById(R.id.ingredientsRecyclerView) // Exibe a lista de ingredientes
        val instructions: TextView = findViewById(R.id.instructions) // Exibe as instruções de preparo
        val recipeImage: ImageView = findViewById(R.id.recipeImage) // Exibe a imagem da receita

        // Obtém o objeto Recipe passado pela Intent
        val recipe = intent.getSerializableExtra("RECIPE") as Recipe

        // Preenche os campos de texto com as informações da receita
        recipeName.text = recipe.name
        prepTime.text = "${recipe.prepTime} min"
        rating.text = "${recipe.rating}/5"
        instructions.text = recipe.instructions

        // Verifica se a receita tem uma imagem associada (imageResId ou imageUri) e exibe a imagem
        when {
            recipe.imageResId != null -> {
                // Se a imagem for um recurso do app, usa o método setImageResource para exibi-la
                recipeImage.setImageResource(recipe.imageResId)
            }
            recipe.imageUri != null -> {
                // Se a imagem for fornecida via URI, tenta carregar a imagem a partir do arquivo
                val imageFile = File(recipe.imageUri)
                if (imageFile.exists()) {
                    // Cria um URI compatível para acessar o arquivo de imagem
                    val imageUri = FileProvider.getUriForFile(this, "${packageName}.provider", imageFile)
                    recipeImage.setImageURI(imageUri) // Exibe a imagem no ImageView
                } else {
                    // Se a imagem não existir, exibe uma imagem de placeholder
                    recipeImage.setImageResource(R.drawable.recipe_placeholder)
                }
            }
            else -> {
                // Se não houver imagem, exibe uma imagem de placeholder
                recipeImage.setImageResource(R.drawable.recipe_placeholder)
            }
        }

        // Configura o RecyclerView para exibir a lista de ingredientes
        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this) // Usando LinearLayoutManager para exibir os itens em uma lista vertical
        // Cria o adaptador com a lista de ingredientes (separados por linha) e define o adaptador do RecyclerView
        ingredientsRecyclerView.adapter = IngredientAdapter(recipe.ingredients.split("\n"))
    }
}
