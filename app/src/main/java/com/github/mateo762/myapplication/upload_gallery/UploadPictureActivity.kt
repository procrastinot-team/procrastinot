package com.github.mateo762.myapplication.upload_gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.mateo762.myapplication.R

class UploadPictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_picture)

        val selectPictureButton = findViewById<Button>(R.id.select_picture_button)
    }
}