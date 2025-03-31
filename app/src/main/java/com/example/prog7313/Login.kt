package com.example.prog7313

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLog = findViewById<Button>(R.id.btnLog)

        // Handle Login button click
        btnLog.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  // Navigate to MainActivity or Dashboard
            startActivity(intent)
        }
    }
}
