package com.example.prog7313

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserCategoryDao {
    @Insert
    suspend fun insert(category: UserCategoryData)

    @Query("SELECT * FROM categories WHERE transactionType = :transactionType")
    suspend fun getCategoriesByType(transactionType: String): List<UserCategoryData>
}