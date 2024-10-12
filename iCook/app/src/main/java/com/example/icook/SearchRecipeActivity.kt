package com.example.icook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SearchRecipeActivity : AppCompatActivity() {

    private lateinit var btnSearch: Button
    private lateinit var searchField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        btnSearch = findViewById(R.id.btnSearch)
        searchField = findViewById(R.id.searchField)

        btnSearch.setOnClickListener {
            val query = searchField.text.toString()
            val intent = Intent(this, RecipeResultActivity::class.java)
            intent.putExtra("search_query", query)
            startActivity(intent)
        }
    }
}
