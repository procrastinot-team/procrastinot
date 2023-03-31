package com.github.mateo762.myapplication.search

import android.os.Bundle
import android.view.MenuItem
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityProfileBinding
import com.github.mateo762.myapplication.databinding.ActivitySearchBinding
import com.github.mateo762.myapplication.profile.ProfileGalleryAdapter
import com.github.mateo762.myapplication.profile.ProfileGalleryItem
import com.github.mateo762.myapplication.profile.SearchItem
import com.github.mateo762.myapplication.profile.SearchViewAdapter


class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreateDrawer()
        setupToolbar()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager

        val items = listOf(
            SearchItem("Pedro", "Son of Harry"),
            SearchItem("Alice", "Married to Bob"),
            SearchItem("Peter", "Married to Mary"),
            SearchItem("Elisabeth", "Mother of Pascal"),
            SearchItem("Charlotte", "Daughter of Boris")
        )

        adapter = SearchViewAdapter(items)

        binding.recyclerView.adapter = adapter

        // Update adapter's filter when search bar text changes
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            adapter.filter(text.toString())
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