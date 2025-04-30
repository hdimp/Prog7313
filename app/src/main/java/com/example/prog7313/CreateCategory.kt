package com.example.prog7313

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_category)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val editCategoryName = findViewById<EditText>(R.id.editCategoryName)
        val btnCreateCategory = findViewById<Button>(R.id.btnCreateCategory)

        btnCreateCategory.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val type = if (selectedId == R.id.radioIncome) "Income" else "Expense"
            val name = editCategoryName.text.toString()

            if (name.isNotBlank()) {
                val category = UserCategoryData(name = name, transactionType = type)
                lifecycleScope.launch {
                    AppDatabase.getDatabase(applicationContext).userCategoryDao().insert(category)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CreateCategory, "Category Created!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}