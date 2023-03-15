package com.github.mateo762.myapplication.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding

/**
 * Activity for displaying the profile information.
 */
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: ProfileGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        adapter = ProfileGalleryAdapter()
        adapter.galleryItems = generateTextGalleryItems()
        binding.recyclerView.adapter = adapter

        binding.name.text = "John Doe" //todo remove, used for demo and testing
        binding.username.text = "johndoe12345" //todo remove, used for demo and testing
    }

    private fun setupToolbar() {
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

    private fun generateTextGalleryItems(): ArrayList<ProfileGalleryItem> {
        return arrayListOf(
            ProfileGalleryItem(R.drawable.ic_profile),
            ProfileGalleryItem(R.drawable.ic_calendar),
            ProfileGalleryItem(R.drawable.ic_share),
            ProfileGalleryItem(R.drawable.ic_friends),
            ProfileGalleryItem(R.drawable.ic_label),
            ProfileGalleryItem(R.drawable.ic_settings),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
        )
    }
}