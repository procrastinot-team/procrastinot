package com.github.mateo762.myapplication.about

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AboutActivity : BaseActivity() {

    private lateinit var users: TextView
    private lateinit var posts: TextView
    private lateinit var compl: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        users = findViewById(R.id.users)
        posts = findViewById(R.id.posts)
        compl = findViewById(R.id.compl)
        getData()
        super.onCreateDrawer()
    }

    fun getData() {
        val db: DatabaseReference = Firebase.database.reference
        db.child("usernames").get().addOnSuccessListener {
            users.text = it.childrenCount.toString()
        }.addOnFailureListener{ Log.e("firebase", "Error getting data", it) }
        db.child("users").get().addOnSuccessListener {
            val users = it.children // get all users
            var postsCount = 0
            var complCount = 0
            for (user in users) {
                val habitsPath = user.child("habitsPath").value.toString()
                val compl = user.child("imagesPath").value.toString()

                if (habitsPath != "null") {
                    val habits = user.child("habitsPath").childrenCount
                    postsCount += habits.toInt()
                }
                if (compl != "null") {
                    val compls = user.child("imagesPath").childrenCount
                    complCount += compls.toInt()
                }
            } ; posts.text = postsCount.toString() ; compl.text = complCount.toString()
        }.addOnFailureListener{  Log.e("firebase", "Error getting data", it)  }
    }





}