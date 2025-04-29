package com.example.prog7313

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Transactions : AppCompatActivity() {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var textViewSelectCategory: TextView
    private var selectedCategory: String = ""

    private lateinit var uploadPhotoButton: TextView
    private var selectedImageUri: Uri? = null

    private val categoryActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val selectedCategory = data?.getStringExtra("selectedCategory")

            selectedCategory?.let {
                textViewSelectCategory.text = it
            }
        }
    }

    private  val photoActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            @Suppress("DEPRECATION")
            selectedImageUri = data?.getParcelableExtra<Uri>("selectedImageUri")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transactions)

        val database = AppDatabase.getDatabase(this)
        val transactionDao = database.transactionDao()
        val repository = TransactionRepo(transactionDao)
        transactionViewModel = ViewModelProvider(this, TransactionViewModelFactory(repository)).get(TransactionViewModel::class.java)

        val radioGroupTransactionType = findViewById<RadioGroup>(R.id.transactionTypeGroup)
        val radioIncome = findViewById<RadioButton>(R.id.rbIncome)
        val radioExpense = findViewById<RadioButton>(R.id.rbExpense)
        val editTextAmount = findViewById<EditText>(R.id.etAmount)
        val editTextNotes = findViewById<EditText>(R.id.etNotes)
        val buttonSubmit = findViewById<Button>(R.id.btnSubmit)

        textViewSelectCategory = findViewById(R.id.tvSelectCategory)

        textViewSelectCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            val transactionType = when {
                radioIncome.isChecked -> "Income"
                radioExpense.isChecked -> "Expense"
                else -> {
                    Toast.makeText(this, "Please Select Income or Expense.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            intent.putExtra("transactionType", transactionType)
            categoryActivityLauncher.launch(intent)
        }

        uploadPhotoButton = findViewById(R.id.tvUploadPhoto)

        uploadPhotoButton.setOnClickListener {
            val intent = Intent(this, UploadPhoto::class.java)
            photoActivityLauncher.launch(intent)
        }

        buttonSubmit.setOnClickListener {
            val transactionType = when {
                radioIncome.isChecked -> "Income"
                radioExpense.isChecked -> "Expense"
                else -> ""
            }

            if (transactionType.isNotEmpty()) {
                val amount = editTextAmount.text.toString().toDoubleOrNull()
                val notes = editTextNotes.text.toString()
                val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

                if (amount != null) {
                    val transactionData = TransactionData(
                        transactionType = transactionType,
                        date = currentDate,
                        time = currentTime,
                        amount = amount,
                        notes = notes,
                        category = selectedCategory,
                        imageUrl = selectedImageUri.toString()
                    )

                    transactionViewModel.insertTransaction(transactionData)

                    Toast.makeText(this, "Transaction Captured!", Toast.LENGTH_SHORT).show()

                    editTextAmount.text.clear()
                    editTextNotes.text.clear()
                } else {
                    Toast.makeText(this, "Please enter a valid amount!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select a transaction type!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}