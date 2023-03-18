package com.github.procrastinotteam.procrastinot.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.procrastinotteam.procrastinot.R
import com.github.procrastinotteam.procrastinot.databinding.ActivityProfileBinding

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

    private fun generateTextGalleryItems(): ArrayList<ProfileGalleryItem> {
        return arrayListOf(
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
            ProfileGalleryItem(R.drawable.ic_new),
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