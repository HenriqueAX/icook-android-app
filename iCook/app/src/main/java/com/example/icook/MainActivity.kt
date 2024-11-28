package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    // Variáveis para autenticação e interface do usuário
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    // Componentes de interface e banco de dados
    private lateinit var textViewName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configura FirebaseAuth e o cliente de login com Google
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Liga os componentes visuais ao código e inicializa o helper do banco de dados
        textViewName = findViewById(R.id.name)
        progressBar = findViewById(R.id.progress_bar)
        dbHelper = DatabaseHelper(this)

        // Checa se o banco de dados possui receitas; se não, adiciona receitas padrão
        if (dbHelper.getAllRecipes().isEmpty()) {
            addDefaultRecipes()
        }

        // Obtém o nome do usuário da Intent e configura a interface inicial
        val userName = intent.getStringExtra("USER_NAME")
        if (userName != null) {
            textViewName.text = "Bem-vindo, $userName"
            progressBar.isVisible = true

            // Espera 3 segundos e redireciona para a próxima tela
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToSearchRecipeActivity(userName)
            }, 3000)
        } else {
            // Exibe mensagem de erro caso o nome do usuário seja nulo
            textViewName.text = "Erro ao carregar o usuário"
        }
    }

    // Adiciona receitas padrão ao banco de dados
    private fun addDefaultRecipes() {
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

        dbHelper.addRecipe(recipe1)
        dbHelper.addRecipe(recipe2)
        dbHelper.addRecipe(recipe3)
    }

    // Redireciona para a Activity de busca de receitas, passando o nome do usuário
    private fun navigateToSearchRecipeActivity(userName: String) {
        val intent = Intent(this, SearchRecipeActivity::class.java)
        intent.putExtra("USER_NAME", userName) // Passa dados para a próxima tela
        startActivity(intent)
        finish()
    }
}
