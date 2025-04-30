package com.example.prog7313

class BudgetRepo(private val dao: BudgetDao) {

    suspend fun setBudget(amount: Double) {
        dao.setBudget((Budget(monthlyLimit = amount)))
    }

    suspend fun getBudget(): Double {
        return dao.getBudget()?.monthlyLimit ?: 0.0
    }
}