package com.example.icook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para exibir uma lista de ingredientes em um RecyclerView
class IngredientAdapter(private val ingredients: List<String>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    // Classe interna ViewHolder para gerenciar os componentes de cada item da lista
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bulletPoint: TextView = view.findViewById(R.id.bulletPoint) // Indicador visual para o ingrediente
        val ingredientText: TextView = view.findViewById(R.id.ingredientText) // Texto do ingrediente
    }

    // Método chamado para criar um novo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla o layout XML do item (item_ingredient) para criar a visualização do item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder(view) // Retorna o ViewHolder associado ao layout inflado
    }

    // Método chamado para vincular dados a uma posição específica do ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientText.text = ingredients[position] // Define o texto do ingrediente no TextView
        // O ponto (bullet) já está no layout e não precisa ser configurado aqui
    }

    // Retorna o número total de itens na lista
    override fun getItemCount() = ingredients.size
}
