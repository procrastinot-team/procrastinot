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
    private lateinit var dropdownItems: Array<String>
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var dropdownAdapter: ArrayAdapter<String>
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
        var db = Firebase.database.reference
        var refUsers = db.child("users").child(currentUser).child("habitsPath")
        refUsers.get().addOnSuccessListener {
            if (it.exists()) {
                var children = it.children
                for (child in children) {
                    // write child value to object
                    var habitId = child.key.toString()
                    var habitName = child.child("name").value.toString()
                    var trainer = child.child("coach").value
                    habitNames += habitName
                    habitIds += habitId
                    habitTrainerIds += trainer.toString()
                }
                Log.d("habitName", habitNames.toString())
                Log.d("habitId", habitIds.toString())
                Log.d("habitTrainer", habitTrainers.toString())
                Log.d("habitTrainerId", habitTrainerIds.toString())
                dropdownItems = Array(habitNames.size) { i -> habitNames[i] }
                autoCompleteTextView = findViewById(R.id.auto_complete_txt)
                dropdownAdapter = ArrayAdapter<String>(this, R.layout.list_item, dropdownItems)
                autoCompleteTextView.setAdapter(dropdownAdapter)
                takePhotoButton.text = getString(R.string.take_photo)
                takePhotoButton.isEnabled = true
            } else {
                Log.d("habitName", "No such document")
                textInputLayout = findViewById(R.id.textInputLayout)
                textInputLayout.visibility = View.GONE
                takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
                takePhotoButton.isEnabled = false
                takePhotoButton.text = getString(R.string.no_habit_found)
            }
        }.addOnFailureListener {
            // go to home
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun getTrainerArray() {
        var db = Firebase.database.reference
        var refUsernames = db.child("usernames")
        // map ids to usernames
        var mapIdtoUser = HashMap<String, String>()
        refUsernames.get().addOnSuccessListener {
            if (it.exists()) {
                var children = it.children
                for (child in children) {
                    var username = child.key.toString()
                    var id = child.value.toString()
                    mapIdtoUser[id] = username
                }
            }
            for (trainerId in habitTrainerIds) {
                habitTrainers += if (trainerId != "null") {
                    var trainerUsername = mapIdtoUser[trainerId]
                    trainerUsername.toString()
                } else {
                    "No trainer"
                }
            }
            Log.d("gta habitTrainer", habitTrainers.toString())
            Log.d("gta habitTrainerId", habitTrainerIds.toString())
            autoCompleteTextView.setOnItemClickListener() { parent, view, position, id ->
                selectedHabit = parent.getItemAtPosition(position).toString()
                selectedHabitId = habitIds[position]
                selectedTrainer = habitTrainers[position]
                selectedTrainerId = habitTrainerIds[position]
                Toast.makeText(this, selectedHabit + " - " + selectedTrainer, Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun startPage() {
        backHomeButton = findViewById(R.id.backHomeButton)
        backHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity.HomeEntryPoint::class.java)
            startActivity(intent)
        }
        textInputLayout = findViewById(R.id.textInputLayout)
        autoCompleteTextView = findViewById(R.id.auto_complete_txt)

        checkIfUserHasHabits()
        getTrainerArray()

        imageView = findViewById(R.id.imageView)
        takePhotoText = findViewById(R.id.textView)
        takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        takePhotoLoader = findViewById(R.id.progressLoader)
        ratingBar = findViewById(R.id.ratingBar)
        takePhotoButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
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
        if (selectedHabit == null) {
            Toast.makeText(this,getString(R.string.select_a_habit), Toast.LENGTH_SHORT).show()
            return
        }
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, getString(R.string.image_captured), Toast.LENGTH_SHORT).show()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            imageData = data
            onImageCaptured()
        } else {
            Toast.makeText(this, getString(R.string.no_image_captured), Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onImageCaptured() {
        takePhotoButton.visibility = View.GONE
        takePhotoText.visibility = View.VISIBLE
        takePhotoLoader.visibility = View.VISIBLE
        textInputLayout.visibility = View.GONE
        uploadImage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage() {
        // write to firebase storage
        //Generate a file name based on the upload time
        if (imageData == null) {
            Toast.makeText(this, getString(R.string.no_image_data), Toast.LENGTH_SHORT).show()
            // go to home
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            return
        }
        val formatter = SimpleDateFormat(getString(R.string.file_name_date), Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("users/$currentUser/images/$fileName")

        //Upload the image to firebase storage
        storage.putBytes(imageData).addOnSuccessListener {
            //show toast
            Toast.makeText(this, getString(R.string.image_uploaded), Toast.LENGTH_SHORT).show()
            // get the image url
            storage.downloadUrl.addOnSuccessListener {
                var imageUrl = it.toString()
                // write to firebase database
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
                if (selectedTrainer == "No trainer" || selectedTrainer == null) {
                    takePhotoText.text = getString(R.string.done)
                    backHomeButton.visibility = View.VISIBLE
                    backHomeButton.isEnabled = true
                } else {
                    takePhotoText.text = getString(R.string.rank) + selectedTrainer + getString(R.string.as_a_trainer)
                    ratingBar.visibility = View.VISIBLE
                    // listen for rating bar changes
                    var db = Firebase.database.reference
                    var rate = 0.0f
                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        rate = rating
                        if (selectedTrainerId != null) {
                            var refBar = db.child("ratings").child(selectedTrainerId!!).child(currentUser).setValue(rating)
                            takePhotoText.text = getString(R.string.done)
                            backHomeButton.visibility = View.VISIBLE
                            backHomeButton.isEnabled = true
                        } else
                        {
                            Toast.makeText(this, getString(R.string.there_was_an_error), Toast.LENGTH_SHORT).show()
                            // go to home
                            val intent = Intent(this, HomeActivity.HomeEntryPoint::class.java)
                            startActivity(intent)
                        }
                    }
                }


                Thread.sleep(2000)
                takePhotoLoader.visibility = View.GONE
            }

        }
    }

}