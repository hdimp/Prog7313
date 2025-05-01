package com.example.prog7313

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prog7313.R.anim.slide_in_right
import com.example.prog7313.R.anim.slide_out_left
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import androidx.core.widget.addTextChangedListener
import kotlin.math.roundToInt


class HomepageActivity : AppCompatActivity() {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var tvTransactionalItems: TextView
    private lateinit var tvTotalBalance: TextView
    private lateinit var tvCurrentDate: TextView
    private lateinit var etMinGoal: EditText
    private lateinit var etMaxGoal: EditText
    private lateinit var progressBar: ProgressBar
    private var totalBalanceValue = 0.0
    private var minGoalValue = 0.0
    private var maxGoalValue = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)

        setupNavigation()

        // Total Balance

        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        tvTotalBalance = findViewById(R.id.tvBalance)

        viewModel.balanceLiveData.observe(this) { balance ->
            tvTotalBalance.text = "R %.2f".format(balance)
            totalBalanceValue = balance

            if (totalBalanceValue > 0) {
                progressBar.max = totalBalanceValue.roundToInt()
            } else {
                progressBar.max = 1
            }

            updateProgressAndGoalLines()
        }

        viewModel.calculateBalance()

        // Current date

        tvCurrentDate = findViewById(R.id.tvDate)

        val date = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())
        tvCurrentDate.text = date

        // Income and Expense Totals

        val tvIncomeAmount: TextView = findViewById(R.id.tvIncomeAmount)
        val tvIncomeDisplay: TextView = findViewById(R.id.tvIncomeTotalDisplay)
        val tvExpenseAmount: TextView = findViewById(R.id.tvExpenseAmount)

        viewModel.totalIncome.observe(this) { income ->
            val formatted = "R %.2f".format(income)
            tvIncomeAmount.text = formatted
            tvIncomeDisplay.text = formatted
        }

        viewModel.totalExpenses.observe(this) { expenses ->
            tvExpenseAmount.text = "R %.2f".format(expenses)
        }

        // Progress bar and min / max values

        viewModel.loadMonthlyExpense()

        etMinGoal = findViewById(R.id.etMinGoal)
        etMaxGoal = findViewById(R.id.etMaxGoal)
        progressBar = findViewById(R.id.progressBar)

        val prefs = getSharedPreferences("budget_prefs", MODE_PRIVATE)
        val savedMin = prefs.getFloat("min_goal", 0f)
        val savedMax = prefs.getFloat("max_goal", 0f)

        etMinGoal.setText(savedMin.toString())
        etMaxGoal.setText(savedMax.toString())

        viewModel.setMinGoal(savedMin.toDouble())
        viewModel.setMaxGoal(savedMax.toDouble())

        etMinGoal.addTextChangedListener {
            minGoalValue = it.toString().toDoubleOrNull() ?: 0.0
            viewModel.setMinGoal(minGoalValue)
            updateProgressAndGoalLines()
        }

        etMaxGoal.addTextChangedListener {
            maxGoalValue = it.toString().toDoubleOrNull() ?: 0.0
            viewModel.setMaxGoal(maxGoalValue)
            viewModel.refreshMonthlyExpense()
            updateProgressAndGoalLines()
        }

        viewModel.progressPercent.observe(this) { percent ->
            progressBar.progress = percent
            progressBar.post {
                updateProgressAndGoalLines(percent)
            }
        }

        // Category totals list view

        tvTransactionalItems = findViewById(R.id.tvtransactionalItems)

        val dao = AppDatabase.getDatabase(application).transactionDao()
        val repository = TransactionRepo(dao)
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        transactionViewModel.getTotalSpentByCategory().observe(this, Observer { categoryTotals ->

            val displayText = categoryTotals.joinToString("\n") {
                "${it.category}: R ${it.totalSpent}"
            }
            tvTransactionalItems.text = displayText
        })
    }

    private fun updateProgressAndGoalLines(currentPercent: Int = progressBar.progress) {
        val minGoalLine: View = findViewById(R.id.mindGoalLine)
        val maxGoalLine: View = findViewById(R.id.maxGoalLine)

        if (totalBalanceValue == 0.0) return

        val minGoalProgress = ((minGoalValue / totalBalanceValue) * 100).coerceIn(0.0, 100.0)
        val maxGoalProgress = ((maxGoalValue / totalBalanceValue) * 100).coerceIn(0.0, 100.0)

        progressBar.progressTintList = when {
            currentPercent < minGoalProgress -> ColorStateList.valueOf(Color.BLUE)
            currentPercent <= maxGoalProgress -> ColorStateList.valueOf(Color.GREEN)
            else -> ColorStateList.valueOf(Color.RED)
        }

        if (minGoalProgress > 0) {
            minGoalLine.visibility = View.VISIBLE
            minGoalLine.layoutParams.width = (progressBar.width * (minGoalProgress / 100)).toInt()
            minGoalLine.requestLayout()
        } else {
            minGoalLine.visibility = View.INVISIBLE
        }

        if (maxGoalProgress > 0) {
            maxGoalLine.visibility = View.VISIBLE
            maxGoalLine.layoutParams.width = (progressBar.width * (maxGoalProgress / 100)).toInt()
            maxGoalLine.requestLayout()
        } else {
            maxGoalLine.visibility = View.INVISIBLE
        }
    }

    private fun saveGoals() {
        val prefs = getSharedPreferences("budget_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putFloat("min_goal", etMinGoal.text.toString().toFloatOrNull() ?: 0f)
        editor.putFloat("max_goal", etMaxGoal.text.toString().toFloatOrNull() ?: 0f)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        saveGoals()
    }

    private fun setupNavigation() {
        // Find navigation elements
        val navTimeline = findViewById<LinearLayout>(R.id.navTimeline)
        val navSettings = findViewById<LinearLayout>(R.id.navSettings)
        val buttonAddTransaction = findViewById<Button>(R.id.btnAddTransaction)

        // Set click listeners

        navTimeline.setOnClickListener {
            // Navigate to Timeline Activity
            val intent = Intent(this, Timeline::class.java)
            startActivity(intent)
            // https://www.geeksforgeeks.org/how-to-add-slide-animation-between-activities-in-android/
            overridePendingTransition(slide_in_right, slide_out_left)
        }

        navSettings.setOnClickListener {
            // Navigate to Settings Activity
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            // https://www.geeksforgeeks.org/how-to-add-slide-animation-between-activities-in-android/
            overridePendingTransition(slide_in_right, slide_out_left)
        }

        buttonAddTransaction.setOnClickListener {
            val intent = Intent(this, Transactions::class.java)
            startActivity(intent)
        }
    }
}