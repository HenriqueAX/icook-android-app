package com.example.icook

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import java.io.File

// Adaptador para exibir uma lista de receitas em um ListView
class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) : BaseAdapter() {

    // Retorna o número total de itens (receitas) na lista
    override fun getCount(): Int = recipes.size

    // Retorna o item na posição especificada
    override fun getItem(position: Int): Any = recipes[position]

    // Retorna o ID do item na posição especificada
    override fun getItemId(position: Int): Long = position.toLong()

    // Cria e configura a visualização de cada item no ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Usa o convertView (se disponível) ou infla uma nova view a partir do layout recipe_item
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false)

        // Referências para os componentes visuais dentro do item (TextViews e ImageView)
        val recipeName = view.findViewById<TextView>(R.id.recipeName)
        val prepTime = view.findViewById<TextView>(R.id.prepTime)
        val rating = view.findViewById<TextView>(R.id.recipeRating)
        val recipeImage = view.findViewById<ImageView>(R.id.recipeImage)

        // Obtém a receita na posição especificada
        val recipe = recipes[position]

        // Define o nome, tempo de preparo e avaliação da receita nos TextViews correspondentes
        recipeName.text = recipe.name
        prepTime.text = "${recipe.prepTime} min"
        rating.text = "${recipe.rating}/5"

        // Verifica se a receita tem uma imagem associada (imageResId ou imageUri)
        if (recipe.imageResId != null) {
            // Se a imagem for um recurso do aplicativo, define a imagem no ImageView
            recipeImage.setImageResource(recipe.imageResId)
        } else if (recipe.imageUri != null) {
            // Se a imagem for fornecida pelo usuário, usa o URI para carregar a imagem
            val imageFile = File(recipe.imageUri)
            if (imageFile.exists()) {
                // Cria um URI compatível para acessar o arquivo da imagem
                val imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
                recipeImage.setImageURI(imageUri) // Define a imagem no ImageView
            } else {
                // Se o arquivo não existir, exibe uma imagem de placeholder
                recipeImage.setImageResource(R.drawable.recipe_placeholder)
            }
        } else {
            // Se não houver imagem, exibe a imagem de placeholder
            recipeImage.setImageResource(R.drawable.recipe_placeholder)
        }

        // Configura um clique no item para abrir a tela de detalhes da receita
        view.setOnClickListener {
            // Cria uma Intent para abrir a RecipeDetailActivity e passar a receita selecionada
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe) // Passa a receita selecionada para a próxima tela
            context.startActivity(intent) // Inicia a nova Activity
        }

        return view // Retorna a view configurada para exibição no ListView
    }
}
