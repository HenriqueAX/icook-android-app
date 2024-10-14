package com.example.icook

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import java.io.File

class RecipeAdapter(private val context: Context, private val recipes: List<Recipe>) : BaseAdapter() {

    override fun getCount(): Int = recipes.size

    override fun getItem(position: Int): Any = recipes[position]

    override fun getItemId(position: Int): Long = position.toLong()

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

        // Verifique se a URI da imagem está presente e carregue do armazenamento interno
        if (recipe.imageUri != null) {
            val imageFile = File(recipe.imageUri)
            if (imageFile.exists()) {
                val imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
                recipeImage.setImageURI(imageUri)
            } else {
                recipeImage.setImageResource(R.drawable.recipe_placeholder)
            }
        } else {
            recipeImage.setImageResource(R.drawable.recipe_placeholder)
        }

        view.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE", recipe)
            context.startActivity(intent)
        }

        return view
    }
}
