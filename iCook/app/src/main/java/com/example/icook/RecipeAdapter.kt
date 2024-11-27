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

// Adaptador que conecta a lista de receitas ao ListView
class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) : BaseAdapter() {

    // Retorna o número total de receitas
    override fun getCount(): Int = recipes.size

    // Retorna a receita na posição especificada
    override fun getItem(position: Int): Any = recipes[position]

    // Retorna o ID do item na posição especificada
    override fun getItemId(position: Int): Long = position.toLong()

    // Cria e configura a visualização de cada item no ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Infla ou reutiliza a view do item
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false)

        // Obtém os componentes da interface (nome, tempo, avaliação e imagem)
        val recipeName = view.findViewById<TextView>(R.id.recipeName)
        val prepTime = view.findViewById<TextView>(R.id.prepTime)
        val rating = view.findViewById<TextView>(R.id.recipeRating)
        val recipeImage = view.findViewById<ImageView>(R.id.recipeImage)

        // Obtém a receita da lista
        val recipe = recipes[position]

        // Define os dados da receita nos componentes visuais
        recipeName.text = recipe.name
        prepTime.text = "${recipe.prepTime} min"
        rating.text = "${recipe.rating}/5"

        // Configura a imagem da receita, se disponível
        if (recipe.imageResId != null) {
            recipeImage.setImageResource(recipe.imageResId) // Imagem do recurso
        } else if (recipe.imageUri != null) {
            val imageFile = File(recipe.imageUri)
            if (imageFile.exists()) {
                val imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
                recipeImage.setImageURI(imageUri) // Imagem fornecida pelo usuário
            } else {
                recipeImage.setImageResource(R.drawable.recipe_placeholder) // Imagem placeholder
            }
        } else {
            recipeImage.setImageResource(R.drawable.recipe_placeholder) // Imagem placeholder
        }

        // Configura o clique no item para abrir os detalhes da receita
        view.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe) // Passa a receita para a próxima Activity
            context.startActivity(intent) // Inicia a Activity de detalhes
        }

        return view // Retorna a view configurada para o ListView
    }
}
