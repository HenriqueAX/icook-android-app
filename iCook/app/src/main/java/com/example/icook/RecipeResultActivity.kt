package com.example.icook

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RecipeResultActivity : AppCompatActivity() {

    private lateinit var recipeListView: ListView
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_result)

        recipeListView = findViewById(R.id.recipeListView)

        val query = intent.getStringExtra("QUERY") ?: ""
        val allRecipes = loadRecipes() // Carregar suas receitas
        val filteredRecipes = allRecipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
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
