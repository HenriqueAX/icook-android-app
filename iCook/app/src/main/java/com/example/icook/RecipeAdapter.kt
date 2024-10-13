package com.example.icook

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) : BaseAdapter() {

    override fun getCount(): Int {
        return recipes.size
    }

    override fun getItem(position: Int): Any {
        return recipes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false)

        val recipeName = view.findViewById<TextView>(R.id.recipeName)
        val prepTime = view.findViewById<TextView>(R.id.prepTime)
        val rating = view.findViewById<TextView>(R.id.recipeRating)
        val recipeImage = view.findViewById<ImageView>(R.id.recipeImage)

        val recipe = recipes[position]
        recipeName.text = recipe.name
        prepTime.text = "${recipe.prepTime} min"
        rating.text = "${recipe.rating}/5"

        // Associe a imagem baseada na receita usando o id da imagem
        recipeImage.setImageResource(recipe.imageResId)

        // Navegação para detalhes da receita
        view.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            context.startActivity(intent)
        }

        return view
    }
}
