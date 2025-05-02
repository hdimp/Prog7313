package com.example.prog7313

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

class CategoryActivity : AppCompatActivity() {

    //--------------------------------------------
    //
    //--------------------------------------------

    private lateinit var expenseContainer: LinearLayout
    private lateinit var incomeContainer: LinearLayout
    private lateinit var selectedCategory: String
    private lateinit var categoryViewModel: UserCategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)

        //--------------------------------------------
        //
        //--------------------------------------------

        expenseContainer = findViewById(R.id.expenseCategoriesContainer)
        incomeContainer = findViewById(R.id.incomeCategoriesContainer)

        //--------------------------------------------
        //
        //--------------------------------------------

        val transactionType = intent.getStringExtra("transactionType")

        //--------------------------------------------
        //
        //--------------------------------------------

        if (transactionType == "Income") {
            expenseContainer.visibility = View.GONE
            incomeContainer.visibility = View.VISIBLE
        } else if (transactionType == "Expense") {
            expenseContainer.visibility = View.VISIBLE
            incomeContainer.visibility = View.GONE
        }

        //--------------------------------------------
        //
        //--------------------------------------------

        categoryViewModel = ViewModelProvider(this).get(UserCategoryViewModel::class.java)

        categoryViewModel.loadCategories(transactionType)

        val inflater = LayoutInflater.from(this)

        categoryViewModel.categories.observe(this, Observer<List<UserCategoryData>> { customCategories ->

            val container = if (transactionType == "Expense") {
                findViewById<LinearLayout>(R.id.customExpenseCategoryContainer)
            } else {
                findViewById<LinearLayout>(R.id.customIncomeCategoryContainer)
            }

            container.removeAllViews()

            customCategories?.forEach { category ->
                val categoryView = inflater.inflate(R.layout.category_item, null)

                val categoryIcon = categoryView.findViewById<ImageView>(R.id.ivCategoryIcon)
                val categoryName = categoryView.findViewById<TextView>(R.id.tvCategoryTitle)

                categoryName.text = category.name
                categoryIcon.setImageResource(R.drawable.ic_universal)

                categoryView.setOnClickListener {
                    selectCategory(category.name)
                }

                container.addView(categoryView)
            }
        })

        //--------------------------------------------
        //
        //--------------------------------------------

        setupCategorySelection()

    }

    //--------------------------------------------
    //
    //--------------------------------------------

    private fun setupCategorySelection() {

        //--------------------------------------------
        //
        //--------------------------------------------

        findViewById<TextView>(R.id.tvHousing)?.setOnClickListener { selectCategory("Housing") }
        findViewById<TextView>(R.id.tvHealthCare)?.setOnClickListener { selectCategory("Healthcare") }
        findViewById<TextView>(R.id.tvTakeOut)?.setOnClickListener { selectCategory("Takeout") }
        findViewById<TextView>(R.id.tvGroceries)?.setOnClickListener { selectCategory("Groceries") }
        findViewById<TextView>(R.id.tvTransportation)?.setOnClickListener { selectCategory("Transportation") }
        findViewById<TextView>(R.id.tvUtilities)?.setOnClickListener { selectCategory("Utilities") }
        findViewById<TextView>(R.id.tvEntertainment)?.setOnClickListener { selectCategory("Entertainment") }

        //--------------------------------------------
        //
        //--------------------------------------------

        findViewById<TextView>(R.id.tvSalary)?.setOnClickListener { selectCategory("Salary") }
        findViewById<TextView>(R.id.tvLoan)?.setOnClickListener { selectCategory("Loan") }
        findViewById<TextView>(R.id.tvGift)?.setOnClickListener { selectCategory("Gift") }
    }

    //--------------------------------------------
    //
    //--------------------------------------------

    private fun selectCategory(category: String) {
        selectedCategory = category

        val resultIntent = Intent()
        resultIntent.putExtra("selectedCategory", selectedCategory)
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}