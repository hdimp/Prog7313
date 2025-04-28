package com.example.prog7313

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "transactions")
data class TransactionData (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionType: String,
    val date: String,
    val time: String,
    val amount: Double,
    val notes: String?,
    val category: String,
    val imageUrl: String?
)