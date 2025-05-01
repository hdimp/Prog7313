package com.example.prog7313

import androidx.room.Transaction
import java.sql.Timestamp

class TransactionRepo(private val transactionDao: TransactionDao) {

    suspend fun insertTransaction(transactionData: TransactionData) {
        transactionDao.insertTransaction(transactionData)
    }

    suspend fun getAllTransactions(): List<TransactionData> {
        return transactionDao.getAllTransactions()
    }

    suspend fun getTotalSpentByCategory(): List<CategoryTotal> {
        return transactionDao.getTotalSpentByCategory()
    }

    suspend fun getTotalIncome(): Double {
        return transactionDao.getTotalByType("Income") ?: 0.0
    }

    suspend fun getTotalExpenses(): Double {
        return transactionDao.getTotalByType("Expense") ?: 0.0
    }

    suspend fun getMonthlyExpense(): Double {
        val (startOfMonth, endOfMonth) = getStartAndEndOfCurrentMonth()
        return transactionDao.getMonthlyExpense(startOfMonth, endOfMonth) ?: 0.0
    }

    suspend fun getTransactionsForDate(selectedDateTimestamp: Long): List<TransactionData> {
        val (startOfDay, endOfDay) = getStartAndEndOfDay(selectedDateTimestamp)
        return transactionDao.getTransactionsForDate(startOfDay, endOfDay)
    }
}

private fun getStartAndEndOfDay(selectedDateTimestamp: Long): Pair<Long, Long> {
    val calendar = java.util.Calendar.getInstance()
    calendar.timeInMillis = selectedDateTimestamp

    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    val startOfDay = calendar.timeInMillis

    calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
    calendar.set(java.util.Calendar.MINUTE, 59)
    calendar.set(java.util.Calendar.SECOND, 59)
    calendar.set(java.util.Calendar.MILLISECOND, 999)
    val endOfDay = calendar.timeInMillis

    return Pair(startOfDay, endOfDay)
}

private fun getStartAndEndOfCurrentMonth(): Pair<Long, Long> {
    val calendar = java.util.Calendar.getInstance()

    // Start of month
    calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    val startOfMonth = calendar.timeInMillis

    // End of month
    calendar.add(java.util.Calendar.MONTH, 1)
    calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
    calendar.add(java.util.Calendar.DATE, -1)
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
    calendar.set(java.util.Calendar.MINUTE, 59)
    calendar.set(java.util.Calendar.SECOND, 59)
    calendar.set(java.util.Calendar.MILLISECOND, 999)
    val endOfMonth = calendar.timeInMillis

    return Pair(startOfMonth, endOfMonth)

}