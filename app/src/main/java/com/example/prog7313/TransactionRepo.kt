package com.example.prog7313

import androidx.room.Transaction

class TransactionRepo(private val transactionDao: TransactionDao) {

    suspend fun insertTransaction(transactionData: TransactionData) {
        transactionDao.insertTransaction(transactionData)
    }

    suspend fun getAllTransactions(): List<TransactionData> {
        return transactionDao.getAllTransactions()
    }
}