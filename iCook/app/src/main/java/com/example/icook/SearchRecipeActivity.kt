package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView
    private lateinit var btnSearch: Button
    private lateinit var searchField: EditText
    private lateinit var adapter: RecipeAdapter
    private var allRecipes = listOf<Recipe>() // Lista completa de receitas
    private var filteredRecipes = listOf<Recipe>() // Lista filtrada de receitas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        recipeListView = findViewById(R.id.recipeListView)
        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)

        // Carregue suas receitas de algum lugar (por exemplo, de um banco de dados ou lista estática)
        allRecipes = loadRecipes() // Supondo que você tenha uma função que carrega todas as receitas
        filteredRecipes = allRecipes.toList()

        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter

        btnSearch.setOnClickListener {
            searchRecipes()
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchRecipes() {
        val query = searchField.text.toString()
        val intent = Intent(this, RecipeResultActivity::class.java)
        intent.putExtra("QUERY", query)
        startActivity(intent)
    }

    private fun filterRecipes(query: String) {
        filteredRecipes = if (query.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true)
            }
        }
        adapter = RecipeAdapter(this, filteredRecipes)
        recipeListView.adapter = adapter
    }

    private fun loadRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                "Bolo de Chocolate",
                "40 min",
                5,
                "2 xícaras de farinha de trigo\n" +
                        "1 xícara de chocolate em pó\n" +
                        "2 xícaras de açúcar\n" +
                        "1 xícara de leite\n" +
                        "1/2 xícara de óleo\n" +
                        "3 ovos\n" +
                        "1 colher de sopa de fermento em pó",
                "Primeiro, preaqueça o forno a 180°C. Em uma tigela, misture todos os ingredientes secos. Adicione os ovos, o leite e o óleo, e misture até ficar homogêneo. Por último, adicione o fermento e misture levemente. Despeje a massa em uma forma untada e leve ao forno por cerca de 30 a 40 minutos."
            ),
            Recipe(
                "Bolo de Cenoura",
                "50 min",
                4,
                "2 xícaras de cenoura ralada\n" +
                        "1 xícara de óleo\n" +
                        "2 xícaras de açúcar\n" +
                        "3 ovos\n" +
                        "2 xícaras de farinha de trigo\n" +
                        "1 colher de sopa de fermento em pó",
                "Primeiro, preaqueça o forno a 180°C. No liquidificador, bata a cenoura, o óleo, os ovos e o açúcar. Em uma tigela, misture a farinha de trigo e o fermento. Junte a mistura do liquidificador com os secos e mexa até ficar homogêneo. Despeje em uma forma untada e leve ao forno por cerca de 40 a 50 minutos."
            ),
            Recipe(
                "Bolo de Laranja",
                "35 min",
                4,
                "2 xícaras de farinha de trigo\n" +
                        "1 xícara de açúcar\n" +
                        "1/2 xícara de óleo\n" +
                        "1 xícara de suco de laranja\n" +
                        "3 ovos\n" +
                        "1 colher de sopa de fermento em pó\n" +
                        "Raspas de 1 laranja",
                "Primeiro, preaqueça o forno a 180°C. Em uma tigela, misture todos os ingredientes até ficar homogêneo. Despeje a massa em uma forma untada e leve ao forno por cerca de 30 a 35 minutos."
            )
        )
    }
}
