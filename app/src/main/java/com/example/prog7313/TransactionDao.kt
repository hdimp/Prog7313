package com.example.prog7313

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transactionData: TransactionData)

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionData>
}