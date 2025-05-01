package com.example.prog7313

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.prog7313.R.anim.slide_in_right
import com.example.prog7313.R.anim.slide_out_left
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Timeline : AppCompatActivity() {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateText: TextView
    private lateinit var selectedDateValue: TextView
    private lateinit var selectedDateDisplayLayout: LinearLayout
    private lateinit var transactionsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timeline)

        setupNavigation()

        val database = AppDatabase.getDatabase(applicationContext)
        val transactionDao = database.transactionDao()
        val repository = TransactionRepo(transactionDao)
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        calendarView = findViewById(R.id.calendarView)
        selectedDateText = findViewById(R.id.selectedDateText)
        selectedDateValue = findViewById(R.id.selectedDateValue)
        selectedDateDisplayLayout = findViewById(R.id.selectedDateDisplayLayout)
        transactionsTextView = findViewById(R.id.transactionsTextView)

        calendarView.setOnDateChangeListener {_, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            selectedDateValue.text = SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(selectedDate)

            transactionViewModel.getTransactionsForDate(selectedDate).observe(this, Observer { transactions ->
                displayTransactions(transactions)
            })
        }
    }

    private fun displayTransactions(transactions: List<TransactionData>) {
        val container = findViewById<LinearLayout>(R.id.dateContentLayout)
        container.removeAllViews()

        if (transactions.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = "No transactions for this date."
                textSize = 16f
                setTextColor(Color.GRAY)
            }
            container.addView(emptyView)
            return
        }

        transactions.forEach { transaction ->
            val transactionText = "${transaction.transactionType}: R ${transaction.amount}"
            val transactionTextView = TextView(this).apply {
                text = transactionText
                textSize = 16f
                setTextColor(if (transaction.transactionType == "Expense") Color.RED else Color.GREEN)
            }
            container.addView(transactionTextView)

            transaction.imageUrl?.let { url ->
                val imageLink = TextView(this).apply {
                    text = "View Image"
                    setTextColor(Color.BLUE)
                    setOnClickListener {
                        val imageUri = Uri.parse(url)

                        val imageView = ImageView(this@Timeline).apply {
                            setImageURI(imageUri)
                        }

                        val dialog = AlertDialog.Builder(this@Timeline)
                            .setTitle("Transaction Image")
                            .setView(imageView)
                            .setPositiveButton("Close", null)
                            .create()

                        dialog.show()
                    }
                }

                container.addView(imageLink)
            }
        }
    }

    private fun setupNavigation() {
        // Find navigation elements
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navSettings = findViewById<LinearLayout>(R.id.navSettings)

        // Set click listeners
        navHome.setOnClickListener {
            val intent = Intent(this, HomepageActivity::class.java)
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
    }
}