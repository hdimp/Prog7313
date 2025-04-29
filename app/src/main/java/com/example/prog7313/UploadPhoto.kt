package com.example.prog7313

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UploadPhoto : AppCompatActivity() {

    private lateinit var imageViewPreview: ImageView
    private lateinit var buttonSelectPhoto: Button
    private lateinit var buttonConfirmPhoto: Button
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload_photo)

        imageViewPreview = findViewById(R.id.imgPreview)
        buttonSelectPhoto = findViewById(R.id.btnSelectPhoto)
        buttonConfirmPhoto = findViewById(R.id.btnConfirmPhoto)

        buttonSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        buttonConfirmPhoto.setOnClickListener {
            selectedImageUri?.let {
                val resultIntent = Intent()
                resultIntent.putExtra("selectedImageUri", it.toString())
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            imageViewPreview.setImageURI(selectedImageUri)
        }
    }
}