package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.github.mateo762.myapplication.models.HabitImage
import com.github.mateo762.myapplication.upload_gallery.ImageAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.DayOfWeek

/**
 * Activity for displaying the profile information.
 */
class ProfileActivity : BaseActivity() {

    private val user = FirebaseAuth.getInstance().currentUser
    private lateinit var profileImage:ShapeableImageView
    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: ProfileGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        adapter = ProfileGalleryAdapter()
//        adapter.galleryItems = generateTextGalleryItems(R.drawable.ic_new, 13)
        binding.recyclerView.adapter = adapter

        binding.name.text = getString(R.string.missing_name)
        binding.username.text = user?.email

        profileImage = findViewById<ShapeableImageView>(R.id.profileImage)
        profileImage.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent,1000)
        }


        // Retrieval of list of habits
        // First, we take the reference of the database
        val db: DatabaseReference = Firebase.database.reference
        val ref = db.child("users/${user?.uid}/habitsPath")


        // Then, we create a list of habits and we set the value listener
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
                    val startTime = childSnapshot.child("startTime").getValue(String::class.java)!!
                    val endTime = childSnapshot.child("endTime").getValue(String::class.java)!!

                    val textView = TextView(this@ProfileActivity)
                    params.setMargins(16, 16 + top, 8, 8)
                    textView.text = name
                    textView.textSize = 16F
                    textView.layoutParams = params
                    textViews.add(textView)
                    top += 2
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("Failed to read value." + error.toException())
            }
        })

        val refImg = db.child("users/${user?.uid}/imagesPath")
        refImg.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var imagesList:ArrayList<HabitImage> = arrayListOf()
                var prevView:View = findViewById(R.id.profileGalleryTitle)
                for (i in 0 until textViews.size) {
                    imagesList = arrayListOf()
                    for (snapshot in dataSnapshot.children){
                        val image = snapshot.getValue(HabitImage::class.java)
                        if (image!!.habitId == ids[i]) {
                            image?.let {imagesList.add(image)}
                        }
                    }

                    // Add the TextView to the LinearLayout
                    binding.profileActivity.addView(
                        textViews[i],
                        binding.profileActivity.indexOfChild(prevView)+1)
                    prevView = textViews[i]
                    println(imagesList.size)
                    val recyclerView = RecyclerView(this@ProfileActivity)
                    val adapter = ImageAdapter(imagesList, this@ProfileActivity)
                    recyclerView.layoutManager = LinearLayoutManager(this@ProfileActivity,LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.adapter = adapter
                    binding.profileActivity.addView(
                        recyclerView,
                        binding.profileActivity.indexOfChild(prevView)+1)
                    prevView = recyclerView
                }
            }

            override fun onCancelled(error: DatabaseError) {
                print("Failed to read value." + error.toException())
            }
        })
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                val imageUri = data?.data
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

    private fun generateTextGalleryItems(drawable: Int, size: Int): ArrayList<ProfileGalleryItem> {
        val items = ArrayList<ProfileGalleryItem>()
        repeat(size) {
            items.add(ProfileGalleryItem(drawable))
        }
        return items
    }
}