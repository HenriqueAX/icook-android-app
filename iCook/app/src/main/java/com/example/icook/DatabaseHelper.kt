package com.example.icook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Definições do banco de dados
        private const val DATABASE_NAME = "icook.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_RECIPES = "recipes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PREP_TIME = "prep_time"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_INGREDIENTS = "ingredients"
        private const val COLUMN_INSTRUCTIONS = "instructions"
        private const val COLUMN_IMAGE_RES_ID = "image_res_id"
        private const val COLUMN_IMAGE_URI = "image_uri"
    }

    // Criação da tabela no banco de dados
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_RECIPES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PREP_TIME INTEGER,"
                + "$COLUMN_RATING INTEGER,"
                + "$COLUMN_INGREDIENTS TEXT,"
                + "$COLUMN_INSTRUCTIONS TEXT,"
                + "$COLUMN_IMAGE_RES_ID INTEGER,"
                + "$COLUMN_IMAGE_URI TEXT"
                + ")")
        db?.execSQL(createTable) // Executa a criação da tabela no banco
    }

    // Atualização do banco de dados em caso de mudança de versão
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Adiciona a coluna de URI de imagem caso a versão seja atualizada
            db?.execSQL("ALTER TABLE $TABLE_RECIPES ADD COLUMN $COLUMN_IMAGE_URI TEXT")
        }
    }

    // Adiciona uma receita no banco de dados
    fun addRecipe(recipe: Recipe) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, recipe.name)
            put(COLUMN_PREP_TIME, recipe.prepTime)
            put(COLUMN_RATING, recipe.rating)
            put(COLUMN_INGREDIENTS, recipe.ingredients)
            put(COLUMN_INSTRUCTIONS, recipe.instructions)
            put(COLUMN_IMAGE_RES_ID, recipe.imageResId)
            put(COLUMN_IMAGE_URI, recipe.imageUri)
        }
        db.insert(TABLE_RECIPES, null, values)
        db.close() // Fecha a conexão após a inserção
    }

    // Recupera todas as receitas do banco de dados
    fun getAllRecipes(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES", null)

        if (cursor.moveToFirst()) {
            do {
                // Lê as informações de cada receita armazenada no banco de dados
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val prepTime = cursor.getInt(cursor.getColumnIndex(COLUMN_PREP_TIME))
                val rating = cursor.getInt(cursor.getColumnIndex(COLUMN_RATING))
                val ingredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS))
                val instructions = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS))
                val imageResId = cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_RES_ID))
                val imageUri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI))

                // Cria e adiciona a receita à lista
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
                recipeList.add(recipe)
            } while (cursor.moveToNext()) // Percorre todas as receitas armazenadas
        }
        cursor.close() // Fecha o cursor após a consulta
        db.close() // Fecha a conexão com o banco de dados
        return recipeList // Retorna a lista de receitas
    }
}
