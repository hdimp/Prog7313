package com.example.prog7313

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBudget(budget: Budget)

    @Query("SELECT * FROM budget WHERE id = 1")
    suspend fun getBudget(): Budget?

}