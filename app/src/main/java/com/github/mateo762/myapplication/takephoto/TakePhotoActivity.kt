package com.github.mateo762.myapplication.takephoto

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.BuildConfig
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class TakePhotoActivity : BaseActivity() {

    private lateinit var currentUser: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button
    private lateinit var takePhotoText: TextView
    private lateinit var takePhotoLoader: ProgressBar
    private lateinit var ratingBar: RatingBar
    private lateinit var imageData : ByteArray
    private lateinit var dropdownSpinner: Spinner
    private var habitNames: ArrayList<String> = ArrayList()
    private var habitIds: ArrayList<String> = ArrayList()
    private var habitTrainers: ArrayList<String> = ArrayList()
    private var habitTrainerIds: ArrayList<String> = ArrayList()
    private var selectedHabit: String? = null
    private var selectedHabitId: String? = null
    private var selectedTrainer: String? = null
    private var selectedTrainerId: String? = null
    private lateinit var backHomeButton: Button


    fun checkIfUserHasHabits() {
        var db = Firebase.database.reference; var refUsers = db.child("users").child(currentUser).child("habitsPath")
        refUsers.get().addOnSuccessListener {
            if (it.exists()) {
                var children = it.children
                for (child in children) {
                    var habitId = child.key.toString()
                    var habitName = child.child("name").value.toString()
                    var trainer = child.child("coach").value
                    habitNames += habitName
                    habitIds += habitId
                    habitTrainerIds += trainer.toString()
                }
                takePhotoButton.text = getString(R.string.take_photo)
                takePhotoButton.isEnabled = true
            } else {
                dropdownSpinner.visibility = View.GONE
                takePhotoButton.isEnabled = false
                takePhotoButton.text = getString(R.string.no_habit_found)
            }
            getTrainerArray()
        }.addOnFailureListener { startActivity(Intent(this, HomeActivity.HomeEntryPoint::class.java)) }
    }

    fun getTrainerArray() {
        var db = Firebase.database.reference;
        var refUsernames = db.child("usernames");
        var mapIdtoUser = HashMap<String, String>()
        refUsernames.get().addOnSuccessListener {
            if (it.exists()) { var children = it.children
                for (child in children) {
                    var username = child.key.toString()
                    var id = child.value.toString()
                    mapIdtoUser[id] = username } }
            for (trainerId in habitTrainerIds) {
                habitTrainers += if (trainerId != "null") { var trainerUsername = mapIdtoUser[trainerId]; trainerUsername.toString() } else { "No trainer" } }
            dropDownInit()
        }
    }

    fun dropDownInit() {
        val dropdownItems = habitNames.zip(habitTrainers).map { (habitName, trainer) -> "$habitName - $trainer" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dropdownItems)
        dropdownSpinner.adapter = adapter
        dropdownSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedHabit = habitNames[0]
                selectedHabitId = habitIds[0]
                selectedTrainer = habitTrainers[0]
                selectedTrainerId = habitTrainerIds[0] }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedHabit = habitNames[position]
                selectedHabitId = habitIds[position]
                selectedTrainer = habitTrainers[position]
                selectedTrainerId = habitTrainerIds[position]
                showToast(selectedHabit + " - " + selectedTrainer)
            }
        }
    }

    fun startPage() {
        backHomeButton = findViewById(R.id.backHomeButton)
        dropdownSpinner = findViewById(R.id.spinner)
        takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        dropdownSpinner= findViewById(R.id.spinner)
        backHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity.HomeEntryPoint::class.java)
            startActivity(intent)
        }
        checkIfUserHasHabits()
        imageView = findViewById(R.id.imageView)
        takePhotoText = findViewById(R.id.textView)
        takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        takePhotoLoader = findViewById(R.id.progressLoader)
        ratingBar = findViewById(R.id.ratingBar)
        takePhotoButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun showToast(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCameraPermission()
        var firebaseUser = Firebase.auth.currentUser?.uid
        if (firebaseUser == null) {
            currentUser = "testUser"
        } else {
            currentUser = firebaseUser.toString()
        }
        if(BuildConfig.DEBUG){
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        setContentView(R.layout.activity_takephoto)
        startPage()
        super.onCreateDrawer()
    }

    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            showToast(getString(R.string.image_captured))
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            imageData = data
            onImageCaptured()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onImageCaptured() {
        takePhotoButton.visibility = View.GONE
        takePhotoText.visibility = View.VISIBLE
        takePhotoLoader.visibility = View.VISIBLE
        uploadImage()
    }

    private fun showAfterUpload() {
        if (selectedTrainer == "No trainer" || selectedTrainer == null) {
            takePhotoText.text = getString(R.string.done)
            backHomeButton.visibility = View.VISIBLE
            backHomeButton.isEnabled = true
        } else {
            takePhotoText.text = getString(R.string.rank) + selectedTrainer + getString(R.string.as_a_trainer)
            ratingBar.visibility = View.VISIBLE
            var db = Firebase.database.reference
            var rate = 0.0f
            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                rate = rating
                if (selectedTrainerId != null) {
                    var refBar = db.child("ratings").child(selectedTrainerId!!).child(currentUser).setValue(rating)
                    takePhotoText.text = getString(R.string.done)
                    backHomeButton.visibility = View.VISIBLE
                    backHomeButton.isEnabled = true
                }
            }
        }
        takePhotoLoader.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage() {
        if (imageData == null) {
            showToast(getString(R.string.no_image_data))
            startActivity(Intent(this, HomeActivity::class.java))
            return }
        val formatter = SimpleDateFormat(getString(R.string.file_name_date), Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("users/$currentUser/images/$fileName")
        storage.putBytes(imageData).addOnSuccessListener {
            storage.downloadUrl.addOnSuccessListener {
                var imageUrl = it.toString()
                var db = Firebase.database.reference
                if (selectedTrainerId != null) {
                    val habitImage = HabitImageEntity(
                        id = UUID.randomUUID().toString(),
                        userId = currentUser,
                        habitId = selectedHabitId!!,
                        url = imageUrl,
                        date = LocalDateTime.now().toString()
                    )
                    db.child("users").child(currentUser).child("imagesPath").push().setValue(habitImage)
                }
                showAfterUpload()
            }
        }
    }
}