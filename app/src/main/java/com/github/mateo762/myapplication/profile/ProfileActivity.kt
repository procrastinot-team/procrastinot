package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
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
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        binding.name.text = user?.displayName
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

        val refImg = db.child("users/${user?.uid}/imagesPath")
        refImg.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var imagesList:ArrayList<HabitImage>
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
                        val image = snapshot.getValue(HabitImage::class.java)
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
}