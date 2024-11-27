package com.example.icook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para exibir uma lista de ingredientes em um RecyclerView
class IngredientAdapter(private val ingredients: List<String>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    // ViewHolder para gerenciar a visualização de cada item da lista
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bulletPoint: TextView = view.findViewById(R.id.bulletPoint)
        val ingredientText: TextView = view.findViewById(R.id.ingredientText)
    }

    // Cria uma nova instância de ViewHolder com o layout do item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view)
    }

    // Vincula os dados do ingrediente ao ViewHolder correspondente
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientText.text = ingredients[position]
    }

    // Retorna o número de itens na lista
    override fun getItemCount() = ingredients.size
}
