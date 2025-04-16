package com.example.prog7313

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Transcations : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transcations)

        
        val selectCategory = findViewById<TextView>(R.id.tvSelectCategory)
        selectCategory.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)

            val uploadPhoto = findViewById<TextView>(R.id.tvUploadPhoto)

            uploadPhoto.setOnClickListener {
                // Shaun add the upload logic here (camera, gallery, etc.)
                Toast.makeText(this, "Upload Photograph clicked", Toast.LENGTH_SHORT).show()
            }

        }
    }
}