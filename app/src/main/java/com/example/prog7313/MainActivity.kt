package com.example.prog7313

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)

        // Handle Login button click
        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)  // Use Login::class.java, NOT LoginActivity
            startActivity(intent)
        }

        // Handle Create Account button click
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, Register::class.java)  // Use Register::class.java, NOT RegisterActivity
            startActivity(intent)
        }
    }
}
