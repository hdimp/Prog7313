package com.example.prog7313

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class UserCategoryAdapter (

    private var categories: List<UserCategoryData>,
            private val onDeleteClick: (UserCategoryData) -> Unit
) : RecyclerView.Adapter<UserCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCustomName)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvName.text = category.name

        holder.ivDelete.setOnClickListener {
            onDeleteClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateData(newList: List<UserCategoryData>) {
        categories = newList
        notifyDataSetChanged()
    }
}