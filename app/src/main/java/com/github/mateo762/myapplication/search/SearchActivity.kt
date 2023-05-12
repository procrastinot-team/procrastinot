package com.github.mateo762.myapplication.search

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.databinding.ActivitySearchBinding
import com.github.mateo762.myapplication.profile.ProfileActivity
import com.github.mateo762.myapplication.profile.SearchItem
import com.github.mateo762.myapplication.profile.SearchViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchActivity : BaseActivity() {

    private val user = FirebaseAuth.getInstance().currentUser
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

        println("Loaded the Search Activity")

        //TODO: Retrieve a list of users from Firebase
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("usernames")

        val users = mutableListOf(SearchItem("UserId", "Username", "Description"))
        adapter = SearchViewAdapter(users) { username ->
            onUserItemClick(username)
        }
        binding.recyclerView.adapter = adapter

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()

                for (childSnapshot in dataSnapshot.children) {
                    val username = childSnapshot.key
                    val userId = childSnapshot.getValue(String::class.java)
                    if (username != null && userId != null) {
                        users.add(SearchItem(userId, username, "Some description (figure out later"))
                    }
                }

                //adapter.notifyDataSetChanged() does not update the recycler view
                adapter = SearchViewAdapter(users) { username ->
                    onUserItemClick(username)
                }
                binding.recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read action")
            }
        })

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

    private fun onUserItemClick(userId: String) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}