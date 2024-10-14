package com.example.icook

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "icook.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_RECIPES = "recipes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PREP_TIME = "prep_time"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_INGREDIENTS = "ingredients"
        private const val COLUMN_INSTRUCTIONS = "instructions"
        private const val COLUMN_IMAGE_RES_ID = "image_res_id" // Coluna para imageResId
        private const val COLUMN_IMAGE_URI = "image_uri" // Coluna para imageUri
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_RECIPES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PREP_TIME INTEGER,"
                + "$COLUMN_RATING INTEGER,"
                + "$COLUMN_INGREDIENTS TEXT,"
                + "$COLUMN_INSTRUCTIONS TEXT,"
                + "$COLUMN_IMAGE_RES_ID INTEGER,"
                + "$COLUMN_IMAGE_URI TEXT" // Nova coluna para imageUri
                + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_RECIPES ADD COLUMN $COLUMN_IMAGE_URI TEXT")
        }
    }

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

    fun getAllRecipes(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES", null)

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

                val recipe = Recipe(id, name, prepTime, rating, ingredients, instructions,
                    if (imageResId == 0) null else imageResId, imageUri)
                recipeList.add(recipe)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return recipeList
    }
}
