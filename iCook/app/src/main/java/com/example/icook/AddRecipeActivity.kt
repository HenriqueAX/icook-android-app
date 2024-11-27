package com.example.icook

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddRecipeActivity : AppCompatActivity() {

    // Declaração dos componentes da interface gráfica
    private lateinit var etRecipeName: EditText         // Campo para o nome da receita
    private lateinit var etPrepTime: EditText          // Campo para o tempo de preparo
    private lateinit var etRating: EditText            // Campo para a avaliação da receita
    private lateinit var etIngredients: EditText       // Campo para os ingredientes
    private lateinit var etInstructions: EditText      // Campo para as instruções de preparo
    private lateinit var btnAdd: Button                // Botão para adicionar a receita
    private lateinit var imgRecipe: ImageView          // Imagem associada à receita
    private lateinit var btnSearchRecipes: ImageButton // Botão para navegar para a tela de busca de receitas
    private var selectedImageUri: Uri? = null          // URI da imagem selecionada pelo usuário
    private lateinit var dbHelper: DatabaseHelper      // Helper para interagir com o banco de dados SQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // Inicializa os componentes da interface com os IDs definidos no layout XML
        etRecipeName = findViewById(R.id.etRecipeName)
        etPrepTime = findViewById(R.id.etPrepTime)
        etRating = findViewById(R.id.etRating)
        etIngredients = findViewById(R.id.etIngredients)
        etInstructions = findViewById(R.id.etInstructions)
        btnAdd = findViewById(R.id.btnAdd)
        imgRecipe = findViewById(R.id.imgRecipe)
        btnSearchRecipes = findViewById(R.id.btnSearchRecipes)

        // Inicializa o banco de dados SQLite para armazenar receitas
        dbHelper = DatabaseHelper(this)

        // Configura o clique na imagem para selecionar uma imagem da galeria
        imgRecipe.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*" // Define o tipo de conteúdo para imagens
            }
            startActivityForResult(intent, 100) // Inicia a seleção de imagem com um código de requisição
        }

        // Configura o botão de adicionar receita
        btnAdd.setOnClickListener {
            addRecipe() // Chama a função para validar e salvar a receita
        }

        // Configura o botão para navegar para a tela de busca de receitas
        btnSearchRecipes.setOnClickListener {
            val intent = Intent(this, SearchRecipeActivity::class.java)
            startActivity(intent) // Navega para a tela de busca
        }
    }

    // Função para adicionar uma nova receita ao banco de dados
    private fun addRecipe() {
        val name = etRecipeName.text.toString() // Obtém o nome da receita
        val prepTime = etPrepTime.text.toString().toIntOrNull() ?: 0 // Obtém o tempo de preparo ou 0
        val rating = etRating.text.toString().toIntOrNull() ?: 0 // Obtém a avaliação ou 0
        val ingredients = etIngredients.text.toString() // Obtém os ingredientes
        val instructions = etInstructions.text.toString() // Obtém as instruções

        // Validação básica dos campos obrigatórios
        if (name.isNotEmpty() && prepTime > 0 && rating in 1..5) {
            val savedImagePath = selectedImageUri?.let { saveImageToInternalStorage(it) } // Salva a imagem localmente
            val recipe = Recipe(
                id = 0, // O ID será gerado automaticamente pelo banco de dados
                name = name,
                prepTime = prepTime,
                rating = rating,
                ingredients = ingredients,
                instructions = instructions,
                imageUri = savedImagePath // Caminho da imagem salva localmente
            )
            dbHelper.addRecipe(recipe) // Insere a receita no banco de dados
            Toast.makeText(this, "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()

            // Retorna para a tela anterior e informa que houve uma alteração
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            // Exibe uma mensagem caso os campos obrigatórios não estejam preenchidos corretamente
            Toast.makeText(
                this,
                "Por favor, preencha todos os campos corretamente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Manipula o retorno da seleção de imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data // Obtém a URI da imagem selecionada
            imgRecipe.setImageURI(selectedImageUri) // Define a imagem no componente ImageView
        }
    }

    // Função para salvar a imagem no armazenamento interno
    private fun saveImageToInternalStorage(uri: Uri): String? {
        val fileName = "recipe_image_${System.currentTimeMillis()}.jpg" // Define um nome único para a imagem
        val file = File(filesDir, fileName) // Cria o arquivo no diretório interno do aplicativo

        try {
            // Copia os dados da URI para o arquivo local
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace() // Log de erro para depuração
            return null // Retorna null em caso de falha
        }

        return file.absolutePath // Retorna o caminho absoluto do arquivo salvo
    }
}
