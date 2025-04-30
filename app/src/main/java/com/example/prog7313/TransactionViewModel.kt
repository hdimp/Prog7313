package com.example.prog7313

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.room.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp

class TransactionViewModel(private val repository: TransactionRepo) : ViewModel() {

    fun insertTransaction(transactionData: TransactionData) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertTransaction(transactionData)
        }
    }

    fun getAllTransactions() = liveData(Dispatchers.IO) {
        emit(repository.getAllTransactions())
    }

    fun getTotalSpentByCategory() = liveData(Dispatchers.IO) {
        emit(repository.getTotalSpentByCategory())
    }

    fun getTransactionsForDate(selectedDateTimestamp: Long) = liveData(Dispatchers.IO) {
        val startOfDay = selectedDateTimestamp
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1
        emit(repository.getTransactionsForDate(startOfDay, endOfDay))
    }
}

class TransactionViewModelFactory(private val repository: TransactionRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}