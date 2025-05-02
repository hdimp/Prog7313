package com.example.prog7313

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class EditCategories : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserCategoryAdapter
    private lateinit var viewModel: UserCategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_categories)

        recyclerView = findViewById(R.id.rvUserCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = UserCategoryAdapter(emptyList()) { category ->
            deleteCategory(category)
        }

        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(UserCategoryViewModel::class.java)

        viewModel.categories.observe(this) { categoryList ->
            adapter.updateData(categoryList)
        }

        viewModel.loadAllCategories()
    }

    private fun deleteCategory(category: UserCategoryData) {
        viewModel.viewModelScope.launch {
            AppDatabase.getDatabase(application).userCategoryDao().delete(category)
            viewModel.loadCategories(category.transactionType)
        }
    }
}