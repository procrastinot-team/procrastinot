package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.Habit
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.DayOfWeek
import java.util.Objects
import java.util.concurrent.CountDownLatch

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
        val ref = db.child("users/${user?.uid}/habit_list")


        // Then, we create a list of habits and we set the value listener
        val list = mutableListOf<Habit>()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // There, we access the snapshot of the db and for each
                // habit we take each value into a variable
                dataSnapshot.children.forEach { childSnapshot ->
                    val name = childSnapshot.child("name").getValue(String::class.java)!!
                    // for the days field, as it's an enum, we have to iterate once again,
                    // and we do it for every not null value, getting back a list of
                    // DayOfWeek object the we wrap as an ArrayList as needed in the Habit.
                    val days = ArrayList(childSnapshot.child("days").children.mapNotNull {
                        enumValueOf<DayOfWeek>(it.value.toString())
                    })
                    val startTime = childSnapshot.child("startTime").getValue(String::class.java)!!
                    val endTime = childSnapshot.child("endTime").getValue(String::class.java)!!
                    // Finally, we create the habit and add it to the list of habits
                    val myObject = Habit(name, days, startTime, endTime)
                    list.add(myObject)
                }
                println("list: ".plus(list))
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 16, 8, 8)

                var prevTextView: TextView? = findViewById<TextView>(R.id.profileGalleryTitle)

                for (habit in list) {
                    val textView = TextView(this@ProfileActivity)
                    textView.text = habit.name
                    textView.textSize = 16F
                    textView.layoutParams = params
                    // Add the TextView to the LinearLayout
                    binding.profileActivity.addView(
                        textView,
                        binding.profileActivity.indexOfChild(prevTextView)+1)
                    prevTextView = textView
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