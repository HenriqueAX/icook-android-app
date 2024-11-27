package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Activity principal de login
class MainActivity : AppCompatActivity() {

    // Declaração dos componentes da interface gráfica
    private lateinit var etUsername: EditText // Campo de texto para o nome de usuário
    private lateinit var etPassword: EditText // Campo de texto para a senha
    private lateinit var btnLogin: Button // Botão de login
    private lateinit var dbHelper: DatabaseHelper // Instância do helper para interagir com o banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa os campos de entrada e o botão de login com os IDs do layout
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Inicializa o banco de dados para poder interagir com ele
        dbHelper = DatabaseHelper(this)

        // Verifica se o banco de dados está vazio e, se necessário, adiciona receitas padrão
        if (dbHelper.getAllRecipes().isEmpty()) {
            addDefaultRecipes() // Função que adiciona receitas de exemplo ao banco
        }

        // Configura o evento de clique no botão de login
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString() // Obtém o nome de usuário digitado
            val password = etPassword.text.toString() // Obtém a senha digitada

            // Valida se as credenciais correspondem ao usuário e senha padrões
            if (username == "admin" && password == "1234") {
                // Se o login for bem-sucedido, navega para a tela de busca de receitas
                val intent = Intent(this, SearchRecipeActivity::class.java)
                startActivity(intent)
            } else {
                // Se as credenciais forem inválidas, exibe uma mensagem de erro
                Toast.makeText(this, "Usuário e senha incorretos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função para adicionar receitas padrão ao banco de dados
    private fun addDefaultRecipes() {
        // Criação de uma receita de exemplo: Bolo de Chocolate
        val recipe1 = Recipe(
            id = 1,
            name = "Bolo de Chocolate",
            prepTime = 40,
            rating = 5,
            ingredients = "2 xícaras de farinha de trigo\n" +
                    "1 xícara de chocolate em pó\n" +
                    "2 xícaras de açúcar\n" +
                    "1 xícara de leite\n" +
                    "1/2 xícara de óleo\n" +
                    "3 ovos\n" +
                    "1 colher de sopa de fermento em pó",
            instructions = "Primeiro, preaqueça o forno a 180°C. Em uma tigela, misture todos os ingredientes secos. Adicione os ovos, o leite e o óleo, e misture até ficar homogêneo. Por último, adicione o fermento e misture levemente. Despeje a massa em uma forma untada e leve ao forno por cerca de 30 a 40 minutos.",
            imageResId = R.drawable.bolo_chocolate
        )

        // Criação de outra receita de exemplo: Bolo de Cenoura
        val recipe2 = Recipe(
            id = 2,
            name = "Bolo de Cenoura",
            prepTime = 50,
            rating = 4,
            ingredients = "2 xícaras de cenoura ralada\n" +
                    "1 xícara de óleo\n" +
                    "2 xícaras de açúcar\n" +
                    "3 ovos\n" +
                    "2 xícaras de farinha de trigo\n" +
                    "1 colher de sopa de fermento em pó",
            instructions = "Primeiro, preaqueça o forno a 180°C. No liquidificador, bata a cenoura, o óleo, os ovos e o açúcar. Em uma tigela, misture a farinha de trigo e o fermento. Junte a mistura do liquidificador com os secos e mexa até ficar homogêneo. Despeje em uma forma untada e leve ao forno por cerca de 40 a 50 minutos.",
            imageResId = R.drawable.bolo_cenoura
        )

        // Criação de uma receita adicional: Bolo de Laranja
        val recipe3 = Recipe(
            id = 3,
            name = "Bolo de Laranja",
            prepTime = 35,
            rating = 4,
            ingredients = "2 xícaras de farinha de trigo\n" +
                    "1 xícara de açúcar\n" +
                    "1/2 xícara de óleo\n" +
                    "1 xícara de suco de laranja\n" +
                    "3 ovos\n" +
                    "1 colher de sopa de fermento em pó\n" +
                    "Raspas de 1 laranja",
            instructions = "Primeiro, preaqueça o forno a 180°C. Em uma tigela, misture todos os ingredientes até ficar homogêneo. Despeje a massa em uma forma untada e leve ao forno por cerca de 30 a 35 minutos.",
            imageResId = R.drawable.bolo_laranja
        )

        // Adiciona as receitas criadas no banco de dados
        dbHelper.addRecipe(recipe1)
        dbHelper.addRecipe(recipe2)
        dbHelper.addRecipe(recipe3)
    }
}
