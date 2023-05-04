package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.upload_gallery.ImageAdapter
import com.github.mateo762.myapplication.username.UsernameActivity
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

/**
 * Activity for displaying the profile information.
 */
class ProfileActivity : BaseActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext = job + Dispatchers.Main

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameTextView: TextView
    private var imageUri: Uri? = null
    private lateinit var btnEdit: ImageButton
    private lateinit var btnSave: ImageButton
    private lateinit var btnFollow: ImageButton
    private lateinit var btnUnfollow: ImageButton
    private lateinit var changeUsernameButton: Button

    private val user = FirebaseAuth.getInstance().currentUser
    private lateinit var profileImage:ShapeableImageView
    lateinit var binding: ActivityProfileBinding
    private val userRepository = UserRepository()
    private lateinit var db: DatabaseReference
    private lateinit var uid: String


    companion object {
        private val TAG = ProfileActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        db = Firebase.database.reference
        profileImage = findViewById(R.id.profileImage)
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        nameTextView = findViewById(R.id.textViewName)
        emailTextView = findViewById(R.id.textViewEmail)
        usernameTextView = findViewById(R.id.textViewUsername)
        changeUsernameButton = findViewById(R.id.changeUsernameButton)
        btnEdit = findViewById(R.id.btnEdit)
        btnSave = findViewById(R.id.btnSave)
        btnFollow = findViewById(R.id.btnFollow)
        btnUnfollow = findViewById(R.id.btnUnfollow)
        nameEditText.isEnabled = false
        nameEditText.isClickable = false
        nameEditText.background = null
        nameEditText.visibility = View.GONE
        emailEditText.isEnabled = false
        emailEditText.isClickable = false
        emailEditText.background = null
        emailEditText.visibility = View.GONE
        changeUsernameButton.visibility = View.GONE
        btnSave.visibility = View.GONE

        uid = intent.getStringExtra("userId") ?: user!!.uid


        if (uid == user!!.uid) {
            btnFollow.visibility = View.GONE
            btnUnfollow.visibility = View.GONE
            btnEdit.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.GONE
            // Check if the user is already following the profile
            launch {
                val isFollowing = userRepository.checkIfUserFollows(user!!.uid, uid)
                if (isFollowing) {

                    // If the user is following the profile, show the 'btnUnfollow' button
                    btnFollow.visibility = View.GONE
                    btnUnfollow.visibility = View.VISIBLE
                } else {

                    btnFollow.visibility = View.VISIBLE
                    btnUnfollow.visibility = View.GONE
                }
            }
        }


        btnFollow.setOnClickListener {
            followUser(user!!.uid, uid)
        }

        btnUnfollow.setOnClickListener {
            unfollowUser(user!!.uid, uid)
        }

        changeUsernameButton.setOnClickListener {
            val intent = Intent(this, UsernameActivity::class.java).apply {
                putExtra(UsernameActivity.OLD_USERNAME_KEY, usernameTextView.text)
            }
            startActivity(intent)
        }

        btnEdit.setOnClickListener {
            // We enable the name and email edit texts such that they can be edited
            nameEditText.isEnabled = true
            nameEditText.isClickable = true
            nameEditText.visibility = View.VISIBLE
            nameEditText.setText(nameTextView.text)
            nameTextView.visibility = View.GONE
            emailEditText.isEnabled = true
            emailEditText.isClickable = true
            emailEditText.visibility = View.VISIBLE
            emailEditText.setText(emailTextView.text)
            emailTextView.visibility = View.GONE
            usernameTextView.visibility = View.GONE
            changeUsernameButton.visibility = View.VISIBLE

            // We hide the edit button and show the save button
            btnEdit.visibility = View.GONE
            btnSave.visibility = View.VISIBLE

            profileImage.setOnClickListener {
                val openGalleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(openGalleryIntent,1000)
            }
        }

        btnSave.setOnClickListener {
            // We save the updated value to your database or data storage
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()

            // We change the EditText's properties back to make it non-editable
            nameTextView.visibility = View.VISIBLE
            nameEditText.isEnabled = false
            nameEditText.isClickable = false
            nameEditText.visibility = View.GONE
            nameTextView.text = newName
            emailTextView.visibility = View.VISIBLE
            emailEditText.isEnabled = false
            emailEditText.isClickable = false
            emailEditText.visibility = View.GONE
            emailTextView.text = newEmail
            usernameTextView.visibility = View.VISIBLE
            changeUsernameButton.visibility= View.GONE

            // We hide the save button and show the edit button
            btnEdit.visibility = View.VISIBLE
            btnSave.visibility = View.GONE

            profileImage.setOnClickListener { }

            db.child("users").child(uid).child("name").setValue(newName)
            db.child("users").child(uid).child("email").setValue(newEmail)

            val storageRef = Firebase.storage.reference
            val imagesRef = storageRef.child("users/${uid}/images/${UUID.randomUUID()}.jpg")
            if (imageUri != null) {
                val uploadTask = imagesRef.putFile(imageUri!!)
                uploadTask.addOnSuccessListener { uri ->
                    db.child("users").child(uid).child("name").setValue(uri.toString())
                }
            }

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

            user?.updateProfile(profileUpdates)
        }




        db.child("users").child(uid).child("url").get().addOnSuccessListener { it ->
            val url = it.getValue(String::class.java)
            Glide.with(this@ProfileActivity)
                .load(url)
                .into(profileImage)
        }

        db.child("users").child(uid).child("name").get().addOnSuccessListener { it ->
            nameTextView.text = it.getValue(String::class.java)
        }
        db.child("users").child(uid).child("email").get().addOnSuccessListener { it ->
            emailTextView.text = it.getValue(String::class.java)
        }

        // Retrieval of list of habits
        // First, we take the reference of the database
        val ref = db.child("users/${uid}/habitsPath")


        // Then, we create a list of names, ids and textViews, where we store habits data
        // and we set the value listener
        val names = mutableListOf<String>()
        val ids = mutableListOf<String>()
        val textViews = mutableListOf<TextView>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                var top = 0

                // There, we access the snapshot of the db and for each
                // habit we take each value into a variable
                dataSnapshot.children.forEach { childSnapshot ->
                    val id = childSnapshot.child("id").getValue(String::class.java)!!
                    ids.add(id)
                    val name = childSnapshot.child("name").getValue(String::class.java)!!
                    names.add(name)
                    // for the days field, as it's an enum, we have to iterate once again,
                    // and we do it for every not null value, getting back a list of
                    // DayOfWeek object the we wrap as an ArrayList as needed in the Habit.
                    val days = ArrayList(childSnapshot.child("days").children.mapNotNull {
                        enumValueOf<DayOfWeek>(it.value.toString())
                    })

                    val textView = TextView(this@ProfileActivity)
                    params.setMargins(16, 16 + top, 8, 8)

                    // We set the textView text to display the name and days of the habit
                    textView.text = name.plus(" : ").plus(days.joinToString(", ") {it.name})
                    textView.textSize = 20F
                    textView.id = R.id.habitImage
                    textView.layoutParams = params
                    textViews.add(textView)
                    top += 10
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("Failed to read value." + error.toException())
            }
        })

        val refImg = db.child("users/${uid}/imagesPath")
        refImg.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var imagesList:ArrayList<HabitImageEntity>
                val prevView:View = findViewById(R.id.profileGalleryTitle)

                // We create a ScrollView to allow the horizontal scroll of images.
                val scrollView = ScrollView(this@ProfileActivity)

                val parentLayout = LinearLayout(this@ProfileActivity)
                parentLayout.orientation = LinearLayout.VERTICAL

                // We insert in the scrollView the layout to allow the vertical scrolling,
                // inside which we'll insert the horizontally scrolling recyclerViews
                scrollView.addView(parentLayout)

                for (i in 0 until textViews.size) {
                    // First, we get the list of images related to the actual habit by scrolling all
                    // the habits images and taking only the ones matching our habit's id
                    imagesList = arrayListOf()
                    for (snapshot in dataSnapshot.children){
                        val image = snapshot.getValue(HabitImageEntity::class.java)
                        if (image!!.habitId == ids[i]) {
                            image.let {imagesList.add(image)}
                        }
                    }

                    parentLayout.addView(
                        textViews[i],
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ))

                    // We create the recyclerView that will contain the images
                    val recyclerView = RecyclerView(this@ProfileActivity)
                    recyclerView.layoutManager = LinearLayoutManager(
                        this@ProfileActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false)

                    val adapter = ImageAdapter(imagesList, this@ProfileActivity)
                    recyclerView.adapter = adapter
                    // Add the RecyclerView to the parent LinearLayout
                    parentLayout.addView(
                        recyclerView,
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                }
                binding.profileActivity.addView(scrollView, binding.profileActivity.indexOfChild(prevView)+1)

            }

            override fun onCancelled(error: DatabaseError) {
                print("Failed to read value." + error.toException())
            }
        })

    }

    override fun onResume() {
        super.onResume()

        db.child("users").child(uid).child("username").get().addOnSuccessListener { it ->
            usernameTextView.text = it.getValue(String::class.java)
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data?.data!!
                profileImage.setImageURI(imageUri)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        title = "" // the title is not set directly on the xml, avoid having two titles per screen
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun followUser(currentUserId: String, targetUserId: String) {
        launch {
            userRepository.followUser(currentUserId, targetUserId)
            btnFollow.visibility = View.GONE
            btnUnfollow.visibility = View.VISIBLE
        }
    }

    private fun unfollowUser(currentUserId: String, targetUserId: String){
        launch {
            userRepository.unfollowUser(currentUserId, targetUserId)
            btnFollow.visibility = View.VISIBLE
            btnUnfollow.visibility = View.GONE
        }
    }
}