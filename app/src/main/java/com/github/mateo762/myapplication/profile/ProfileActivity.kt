package com.github.mateo762.myapplication.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth

/**
 * Activity for displaying the profile information.
 */
class ProfileActivity : AppCompatActivity() {

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
        adapter.galleryItems = generateTextGalleryItems(R.drawable.ic_new, 13)
        binding.recyclerView.adapter = adapter

        binding.name.text = R.string.missing_name.toString()
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
//                uploadImageToFirebase(imageUri)
            }
        }
    }
//
//    private fun uploadImageToFirebase(imageUri: Uri?) {
//        val db: DatabaseReference = Firebase.database.reference
//
//        val fileRef = db.child("users/${user?.uid}/gravatar")
//    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        title = getString(R.string.profile_toolbar_title)
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