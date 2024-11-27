package com.example.icook

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import java.io.File

// Adaptador para gerenciar e exibir a lista de receitas no RecyclerView
class RecipeAdapter(
    private val context: Context,
    private val recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit // Callback para clique no item
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    // ViewHolder para o RecyclerView, mantém as referências dos componentes de cada item
    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeName: TextView = view.findViewById(R.id.recipeName)
        val prepTime: TextView = view.findViewById(R.id.prepTime)
        val rating: TextView = view.findViewById(R.id.recipeRating)
        val recipeImage: ImageView = view.findViewById(R.id.recipeImage)

        // Liga os dados da receita ao ViewHolder
        fun bind(recipe: Recipe) {
            recipeName.text = recipe.name
            prepTime.text = "${recipe.prepTime} min"
            rating.text = "${recipe.rating}/5"

            // Configura a imagem da receita com base nos recursos disponíveis
            when {
                recipe.imageResId != null -> {
                    recipeImage.setImageResource(recipe.imageResId)
                }
                recipe.imageUri != null -> {
                    val imageFile = File(recipe.imageUri)
                    if (imageFile.exists()) {
                        // Obtém o URI seguro para o arquivo de imagem
                        val imageUri: Uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            imageFile
                        )
                        recipeImage.setImageURI(imageUri)
                    } else {
                        // Define uma imagem de placeholder se o arquivo não existir
                        recipeImage.setImageResource(R.drawable.recipe_placeholder)
                    }
                }
                else -> {
                    // Define uma imagem de placeholder se nenhuma imagem estiver disponível
                    recipeImage.setImageResource(R.drawable.recipe_placeholder)
                }
            }

            // Configura o clique no item para acionar o callback
            itemView.setOnClickListener { onItemClick(recipe) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        // Infla o layout do item de receita
        val view = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        // Vincula os dados da receita ao ViewHolder correspondente
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
    fun updateRecipes(filteredRecipes: List<Recipe>) {

    }
}
