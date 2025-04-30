package com.example.prog7313

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

class CategoryActivity : AppCompatActivity() {

    private lateinit var expenseContainer: LinearLayout
    private lateinit var incomeContainer: LinearLayout
    private lateinit var selectedCategory: String
    private lateinit var categoryViewModel: UserCategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)

        expenseContainer = findViewById(R.id.expenseCategoriesContainer)
        incomeContainer = findViewById(R.id.incomeCategoriesContainer)

        val transactionType = intent.getStringExtra("transactionType")

        if (transactionType == "Income") {
            expenseContainer.visibility = View.GONE
            incomeContainer.visibility = View.VISIBLE
        } else if (transactionType == "Expense") {
            expenseContainer.visibility = View.VISIBLE
            incomeContainer.visibility = View.GONE
        }

        categoryViewModel = ViewModelProvider(this).get(UserCategoryViewModel::class.java)

        categoryViewModel.loadCategories(transactionType)

        categoryViewModel.categories.observe(this, Observer<List<UserCategoryData>> { customCategories ->
            customCategories?.forEach { category ->
                val tv = TextView(this).apply {
                    text = category.name
                    textSize = 16f
                    setTextColor(Color.WHITE)
                    setOnClickListener {
                        selectCategory(category.name)
                    }
                }

                if (transactionType == "Expense") expenseContainer.addView(tv)
                else incomeContainer.addView(tv)
            }
        })

        setupCategorySelection()

        val btnSubmitCategory = findViewById<Button>(R.id.btnSubmitCategory)

        btnSubmitCategory.setOnClickListener {
            if (::selectedCategory.isInitialized) {
                val resultIntent = Intent()
                resultIntent.putExtra("selectedCategory", selectedCategory)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCategorySelection() {
        //Expense Categories
        findViewById<TextView>(R.id.tvHousing)?.setOnClickListener { selectCategory("Housing") }
        findViewById<TextView>(R.id.tvHealthCare)?.setOnClickListener { selectCategory("Healthcare") }
        findViewById<TextView>(R.id.tvTakeOut)?.setOnClickListener { selectCategory("Takeout") }
        findViewById<TextView>(R.id.tvGroceries)?.setOnClickListener { selectCategory("Groceries") }
        findViewById<TextView>(R.id.tvTransportation)?.setOnClickListener { selectCategory("Transportation") }
        findViewById<TextView>(R.id.tvUtilities)?.setOnClickListener { selectCategory("Utilities") }
        findViewById<TextView>(R.id.tvEntertainment)?.setOnClickListener { selectCategory("Entertainment") }

        // Income Categories
        findViewById<TextView>(R.id.tvSalary)?.setOnClickListener { selectCategory("Salary") }
        findViewById<TextView>(R.id.tvLoan)?.setOnClickListener { selectCategory("Loan") }
        findViewById<TextView>(R.id.tvGift)?.setOnClickListener { selectCategory("Gift") }
    }

    private fun selectCategory(category: String) {
        selectedCategory = category
    }
}