package com.example.prog7313

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.exp

class HomePageViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: TransactionRepo = TransactionRepo(AppDatabase.getDatabase(application).transactionDao())

    // Income and Expenses

    private val _balanceLiveData = MutableLiveData<Double>()
    val balanceLiveData: LiveData<Double> get() = _balanceLiveData

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> get() = _totalIncome

    private val _totalExpenses = MutableLiveData<Double>()
    val totalExpenses: LiveData<Double> get() = _totalExpenses

    // Budget

    private val _monthlyExpenseLiveData = MutableLiveData<Double>()
    val monthlyExpenseLiveData: LiveData<Double> get() = _monthlyExpenseLiveData

    private val _minGoal = MutableLiveData<Double>()
    val minGoal: LiveData<Double> get() = _minGoal

    private val _maxGoal = MutableLiveData<Double>()
    val maxGoal: LiveData<Double> get() = _maxGoal

    private val _progressPercent = MutableLiveData<Int>()
    val progressPercent: LiveData<Int> get() = _progressPercent

    // Balance calculate

    fun calculateBalance() {
        viewModelScope.launch {
            val income = repo.getTotalIncome()
            val expenses = repo.getTotalExpenses()

            _balanceLiveData.postValue(income - expenses)
            _totalIncome.postValue(income)
            _totalExpenses.postValue(expenses)

            updateProgressBar()
        }
    }

    // Progress bar / budget

    fun loadMonthlyExpense() {
        viewModelScope.launch {
            viewModelScope.launch {
                val expense = repo.getMonthlyExpense()
                _monthlyExpenseLiveData.postValue(expense)
                updateProgressBar()
            }
        }
    }

    private fun updateProgressBar() {
        val max = _maxGoal.value ?: return
        val expense = _monthlyExpenseLiveData.value ?: return

        val percent = if (max > 0) {
            ((expense / max) * 100).coerceAtMost(100.0)
        } else {
            0.0
        }

        _progressPercent.postValue(percent.toInt())
    }

    fun restoreGoals(minGoal: Double, maxGoal: Double) {
        _minGoal.value = minGoal
        _maxGoal.value = maxGoal
        updateProgressBar()
    }

    fun setMinGoal(value: Double) {
        _minGoal.value = value
        updateProgressBar()
    }

    fun setMaxGoal(value: Double) {
        _maxGoal.value = value
        updateProgressBar()
    }

    fun refreshMonthlyExpense() {
        loadMonthlyExpense()
    }
}