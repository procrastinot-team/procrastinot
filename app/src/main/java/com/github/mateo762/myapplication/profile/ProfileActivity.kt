package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.Habit
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Objects

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

        val db: DatabaseReference = Firebase.database.reference

        val ref = db.child("users/${user?.uid}/habit_list").get().addOnCompleteListener() {
            task ->
            val doc = task.result
            println(doc)
//            val u = it.getValue(Habit::class.java) as Habit
//            println(u.toString())
//            val snap = doc.toString()
//            val json = "[".plus(snap.substring(1,snap.length - 1)).plus("]")
//            val gson = Gson()
//            val v = (Habit::class.java)
//            val list = ArrayList<Habit>()
//            for (i in v.values) {
//                list.add(gson.fromJson<Habit>(i,Habit::class.java))
//            }
//            val type = object : TypeToken<ArrayList<Habit>>() {}.type!!
//            val list = gson.fromJson<Habit>(snap,type)
//            println(list)
//            val u = gson.fromJson(it.value.toString(),Habit::class.java)
//            println(u.toString())
        }

        setupToolbar()
        adapter = ProfileGalleryAdapter()
        adapter.galleryItems = generateTextGalleryItems(R.drawable.ic_new, 13)
        binding.recyclerView.adapter = adapter

        binding.name.text = getString(R.string.missing_name)
        binding.username.text = user?.email

        profileImage = findViewById<ShapeableImageView>(R.id.profileImage)
        profileImage.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(openGalleryIntent,1000)
        }
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