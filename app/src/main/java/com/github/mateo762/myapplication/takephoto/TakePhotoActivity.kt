package com.github.mateo762.myapplication.takephoto

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.BuildConfig
import com.github.mateo762.myapplication.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.util.*


class TakePhotoActivity : BaseActivity() {

    private lateinit var currentUser: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button
    private lateinit var takePhotoText: TextView
    private lateinit var takePhotoLoader: ProgressBar
    private lateinit var imageData : ByteArray


    override fun onCreate(savedInstanceState: Bundle?) {
        getCameraPermission()
        // User
        var firebaseUser = Firebase.auth.currentUser?.uid

        if (firebaseUser == null) {
            currentUser = "testUser"
        } else {
            currentUser = firebaseUser.toString()
        }

        Log.d("TakePhotoActivity", "User: $currentUser")

        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            val keyguardLock = km.newKeyguardLock("TAG")
            keyguardLock.disableKeyguard()
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        if(BuildConfig.DEBUG){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        setContentView(R.layout.activity_takephoto)
        imageView = findViewById(R.id.imageView)
        takePhotoText = findViewById(R.id.textView)
        takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        takePhotoLoader = findViewById(R.id.progressLoader)
        takePhotoButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
        super.onCreateDrawer()
    }

    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.d("TakePhotoActivity", "dispatchTakePictureIntent: ${takePictureIntent.resolveActivity(packageManager)}")
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image captured", Toast.LENGTH_SHORT).show()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            imageData = data
            onImageCaptured()
        } else {
            Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onImageCaptured() {
        // hide button
        takePhotoButton.visibility = View.GONE
        // show text
        takePhotoText.visibility = View.VISIBLE
        takePhotoLoader.visibility = View.VISIBLE
    }

}