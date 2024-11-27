package com.example.icook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Nome e versão do banco de dados
        private const val DATABASE_NAME = "icook.db" // Nome do banco de dados
        private const val DATABASE_VERSION = 2 // Versão atual do banco de dados

        // Nome da tabela e colunas
        private const val TABLE_RECIPES = "recipes" // Nome da tabela para armazenar receitas
        private const val COLUMN_ID = "id" // Coluna para o ID único (Primary Key)
        private const val COLUMN_NAME = "name" // Coluna para o nome da receita
        private const val COLUMN_PREP_TIME = "prep_time" // Coluna para o tempo de preparo
        private const val COLUMN_RATING = "rating" // Coluna para a avaliação da receita
        private const val COLUMN_INGREDIENTS = "ingredients" // Coluna para os ingredientes
        private const val COLUMN_INSTRUCTIONS = "instructions" // Coluna para as instruções de preparo
        private const val COLUMN_IMAGE_RES_ID = "image_res_id" // Coluna para o ID de recurso da imagem (opcional)
        private const val COLUMN_IMAGE_URI = "image_uri" // Coluna para o URI da imagem adicionada pelo usuário
    }

    // Criação inicial da tabela no banco de dados
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_RECIPES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," // ID gerado automaticamente
                + "$COLUMN_NAME TEXT," // Nome da receita
                + "$COLUMN_PREP_TIME INTEGER," // Tempo de preparo
                + "$COLUMN_RATING INTEGER," // Avaliação da receita
                + "$COLUMN_INGREDIENTS TEXT," // Lista de ingredientes
                + "$COLUMN_INSTRUCTIONS TEXT," // Instruções de preparo
                + "$COLUMN_IMAGE_RES_ID INTEGER," // ID de recurso da imagem (opcional)
                + "$COLUMN_IMAGE_URI TEXT" // URI da imagem adicionada pelo usuário
                + ")")
        db?.execSQL(createTable) // Executa a criação da tabela
    }

    // Atualização do banco de dados quando a versão é incrementada
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Adiciona a coluna de URI de imagem se a versão for atualizada para 2
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_RECIPES ADD COLUMN $COLUMN_IMAGE_URI TEXT")
        }
    }

    // Adiciona uma nova receita ao banco de dados
    fun addRecipe(recipe: Recipe) {
        val db = this.writableDatabase // Obtém uma instância do banco de dados em modo escrita
        val values = ContentValues().apply {
            put(COLUMN_NAME, recipe.name) // Insere o nome da receita
            put(COLUMN_PREP_TIME, recipe.prepTime) // Insere o tempo de preparo
            put(COLUMN_RATING, recipe.rating) // Insere a avaliação
            put(COLUMN_INGREDIENTS, recipe.ingredients) // Insere os ingredientes
            put(COLUMN_INSTRUCTIONS, recipe.instructions) // Insere as instruções de preparo
            put(COLUMN_IMAGE_RES_ID, recipe.imageResId) // Insere o ID do recurso da imagem (se existir)
            put(COLUMN_IMAGE_URI, recipe.imageUri) // Insere o URI da imagem (se existir)
        }
        db.insert(TABLE_RECIPES, null, values) // Insere os valores na tabela
        db.close() // Fecha a conexão com o banco de dados
    }

    // Recupera todas as receitas do banco de dados
    fun getAllRecipes(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>() // Lista para armazenar as receitas
        val db = this.readableDatabase // Obtém uma instância do banco de dados em modo leitura
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES", null) // Consulta todas as receitas

        if (cursor.moveToFirst()) {
            do {
                // Lê os dados de cada coluna da tabela
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val prepTime = cursor.getInt(cursor.getColumnIndex(COLUMN_PREP_TIME))
                val rating = cursor.getInt(cursor.getColumnIndex(COLUMN_RATING))
                val ingredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS))
                val instructions = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS))
                val imageResId = cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_RES_ID))
                val imageUri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI))

                // Cria um objeto Recipe a partir dos dados da tabela
                val recipe = Recipe(
                    id = id,
                    name = name,
                    prepTime = prepTime,
                    rating = rating,
                    ingredients = ingredients,
                    instructions = instructions,
                    imageResId = if (imageResId == 0) null else imageResId,
                    imageUri = imageUri
                )
                recipeList.add(recipe) // Adiciona a receita à lista
            } while (cursor.moveToNext()) // Move para o próximo registro
        }
        cursor.close() // Fecha o cursor
        db.close() // Fecha a conexão com o banco de dados
        return recipeList // Retorna a lista de receitas
    }
}
