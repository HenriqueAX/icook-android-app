package com.example.icook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Nome e versão do banco de dados
        private const val DATABASE_NAME = "icook.db"
        private const val DATABASE_VERSION = 2

        // Tabela de receitas
        private const val TABLE_RECIPES = "recipes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PREP_TIME = "prep_time"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_INGREDIENTS = "ingredients"
        private const val COLUMN_INSTRUCTIONS = "instructions"
        private const val COLUMN_IMAGE_RES_ID = "image_res_id"
        private const val COLUMN_IMAGE_URI = "image_uri"

        // Tabela de usuários
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PASSWORD = "password"
    }

    // Criação inicial das tabelas no banco de dados
    override fun onCreate(db: SQLiteDatabase?) {
        val createRecipesTable = ("CREATE TABLE $TABLE_RECIPES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PREP_TIME INTEGER,"
                + "$COLUMN_RATING INTEGER,"
                + "$COLUMN_INGREDIENTS TEXT,"
                + "$COLUMN_INSTRUCTIONS TEXT,"
                + "$COLUMN_IMAGE_RES_ID INTEGER,"
                + "$COLUMN_IMAGE_URI TEXT"
                + ")")
        db?.execSQL(createRecipesTable)

        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USER_NAME TEXT,"
                + "$COLUMN_USER_EMAIL TEXT UNIQUE,"
                + "$COLUMN_USER_PASSWORD TEXT"
                + ")")
        db?.execSQL(createUsersTable)
    }

    // Atualização do banco de dados em caso de mudança de versão
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_RECIPES ADD COLUMN $COLUMN_IMAGE_URI TEXT")
        }
    }

    // Função para adicionar uma receita no banco de dados
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
        db.close()
    }

    // Função para obter todas as receitas armazenadas
    fun getAllRecipes(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES", null)

        // Mapeia os resultados do cursor para objetos Recipe
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val prepTime = cursor.getInt(cursor.getColumnIndex(COLUMN_PREP_TIME))
                val rating = cursor.getInt(cursor.getColumnIndex(COLUMN_RATING))
                val ingredients = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS))
                val instructions = cursor.getString(cursor.getColumnIndex(COLUMN_INSTRUCTIONS))
                val imageResId = cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_RES_ID))
                val imageUri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI))

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
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return recipeList
    }

    // Função para adicionar um usuário no banco de dados
    fun addUser(user: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, user.name)
            put(COLUMN_USER_EMAIL, user.email)
            put(COLUMN_USER_PASSWORD, user.password)
        }

        val result = db.insert(TABLE_USERS, null, values)
        db.close()

        return result != -1L // Retorna true se o usuário foi adicionado com sucesso
    }

    // Função para buscar um usuário pelo e-mail
    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD),
            "$COLUMN_USER_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )

        var user: User? = null
        // Se o e-mail foi encontrado, cria um objeto User com os dados
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME))
            val userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD))

            user = User(id, name, userEmail, password)
        }

        cursor.close()
        db.close()
        return user
    }
}
