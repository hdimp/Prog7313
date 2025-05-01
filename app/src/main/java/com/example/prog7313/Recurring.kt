package com.example.prog7313

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Recurring : AppCompatActivity() {

    private lateinit var editStartDate: EditText
    private lateinit var editStartTime: EditText
    private lateinit var editEndDate: EditText
    private lateinit var editEndTime: EditText
    private lateinit var recurrenceGroup: RadioGroup
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recurring)

        editStartTime = findViewById(R.id.editStartTime)
        editStartDate = findViewById(R.id.editStartDate)
        editEndTime = findViewById(R.id.editEndTime)
        editEndDate = findViewById(R.id.editEndDate)
        recurrenceGroup = findViewById(R.id.recurrenceGroup)
        submitButton = findViewById(R.id.btnSubmitRecurring)

        val calendar = Calendar.getInstance()

        editStartDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val date = "$dayOfMonth-${month + 1}-$year"
                    editStartDate.setText(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        editStartTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val time = String.format("%02d:%02d", hourOfDay, minute)
                    editStartTime.setText(time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        editEndDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val date = "$dayOfMonth-${month + 1}-$year"
                    editEndDate.setText(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        editEndTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val time = String.format("%02d:%02d", hourOfDay, minute)
                    editEndTime.setText(time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        submitButton.setOnClickListener {
            val selectedRecurrence = findViewById<RadioButton>(recurrenceGroup.checkedRadioButtonId)
            val recurrenceType = selectedRecurrence?.text.toString()

            val startDate = editStartDate.text.toString()
            val startTime = editStartTime.text.toString()
            val endDate = editEndDate.text.toString()
            val endTime = editEndTime.text.toString()

            if (startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty() || recurrenceType.isEmpty()) {
                Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val startTimestamp = parseToTimestamp(startDate, startTime)
            val endTimestamp = parseToTimestamp(endDate, endTime)

            if (startTimestamp == null || endTimestamp == null) {
                Toast.makeText(this, "Invalid date or time format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("frequency", recurrenceType)
                putExtra("startTimestamp", startTimestamp)
                putExtra("endTimestamp", endTimestamp)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun parseToTimestamp(date: String, time: String): Long? {
        return try {
            val format = java.text.SimpleDateFormat("d-M-yyy HH:mm", java.util.Locale.getDefault())
            val combined = "$date $time"
            format.parse(combined)?.time
        } catch (e: Exception) {
            null
        }
    }
}