package com.example.icook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientAdapter(private val ingredients: List<String>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bulletPoint: TextView = view.findViewById(R.id.bulletPoint)
        val ingredientText: TextView = view.findViewById(R.id.ingredientText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientText.text = ingredients[position]
        // A bolinha já está no layout, então não é necessário configurar aqui
    }

    override fun getItemCount() = ingredients.size
}

