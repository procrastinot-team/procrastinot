package com.github.mateo762.myapplication.about

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
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
        // get the firebase data and set the text
        val db: DatabaseReference = Firebase.database.reference
        db.child("usernames").get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            users.text = it.childrenCount.toString()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        db.child("users").get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            val users = it.children // get all users
            var postsCount = 0
            var complCount = 0
            for (user in users) {
                // get all habits for each user in habitsPath
                val habitsPath = user.child("habitsPath").value.toString()
                Log.d("firebase", "Got value ${habitsPath}")
                if (habitsPath != "null") {
                    val habits = user.child("habitsPath").childrenCount
                    postsCount += habits.toInt()
                    Log.d("firebase", "Got value ${habits}")
                }
                val compl = user.child("imagesPath").value.toString()
                if (compl != "null") {
                    val compls = user.child("imagesPath").childrenCount
                    complCount += compls.toInt()
                    Log.d("firebase", "Got value ${compls}")
                }
            }
            posts.text = postsCount.toString()
            compl.text = complCount.toString()
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }





}