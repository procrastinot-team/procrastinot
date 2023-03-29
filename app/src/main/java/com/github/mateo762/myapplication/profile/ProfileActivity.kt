package com.github.mateo762.myapplication.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding

/**
 * Activity for displaying the profile information.
 */
class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: ProfileGalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_profile)
        super.onCreateDrawer()
        // setContentView(binding.root)

        // setupToolbar() // Remove to use inherited toolbar configuration, activity now changed
        // to BaseActivity, it includes this in its super onCreate()
        adapter = ProfileGalleryAdapter()
        adapter.galleryItems = generateTextGalleryItems(R.drawable.ic_new, 13)
        binding.recyclerView.adapter = adapter

        binding.name.text = "John Doe" //todo remove, used for demo and testing
        binding.username.text = "johndoe12345" //todo remove, used for demo and testing
    }

/*    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        title = getString(R.string.profile_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }*/

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