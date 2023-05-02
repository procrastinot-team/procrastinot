package com.github.mateo762.myapplication.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.models.Post
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.room.HabitImageEntity
import com.github.mateo762.myapplication.ui.home.FeedScreen
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FeedFragment : Fragment() {
    private lateinit var usersRef: DatabaseReference
    private lateinit var imagesRef: DatabaseReference
    private val imagesState = mutableStateOf(emptyList<Post>())

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FeedScreen(posts = imagesState.value)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase database reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            usersRef = FirebaseDatabase.getInstance().getReference("/users/${currentUser.uid}")
            fetchFollowingUsers(currentUser.uid)
        }
    }

    private fun fetchFollowingUsers(userId: String) {
        usersRef.child("followingPath").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val followingUserList =
                        childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                    if (followingUserList != null) {
                        for (followingUserId in followingUserList) {
                            Log.d(TAG, "heyyy: ${followingUserId}")
                            fetchUserImages(followingUserId)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Error: ${error.message}")
            }
        })
    }

    private fun fetchUserImages(userId: String) {
        imagesRef = FirebaseDatabase.getInstance().getReference("/users/$userId/imagesPath")

        imagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedImages = mutableListOf<HabitImageEntity>()
                for (childSnapshot in snapshot.children) {
                    val image = childSnapshot.getValue(HabitImageEntity::class.java)
                    if (image != null) {
                        fetchedImages.add(image)
                    }
                }
                generatePosts(fetchedImages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Error: ${error.message}")
            }
        })
    }

    private fun generatePosts(fetchedImages: List<HabitImageEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            val generatedPosts = mutableListOf<Post>()

            fetchedImages.forEach { image ->
                val username = fetchUsernameForImage(image.userId).await()
                val habitName = fetchHabitNameForImage(image.userId, image.habitId).await()
                val post = Post(
                    "$habitName",
                    "TEST_POST_DESCRIPTION for Post with habitId ${image.habitId} uploaded on ${image.date}",
                    "$username",
                    "Post associated to habit ${image.habitId}",
                    image
                )
                generatedPosts.add(post)
            }

            withContext(Dispatchers.Main) {
                imagesState.value = imagesState.value + generatedPosts
            }
        }
    }

    private fun fetchUsernameForImage(userId: String): Task<String> {
        return FirebaseDatabase.getInstance().getReference("/users/$userId/username")
            .get()
            .continueWith { task ->
                val username = task.result.getValue(String::class.java) ?: ""
                username
            }
    }

    private fun fetchHabitNameForImage(userId: String, habitId: String): Task<String> {
        return FirebaseDatabase.getInstance().getReference("/users/$userId/habitsPath")
            .get()
            .continueWith { task ->
                val dataSnapshot = task.result
                var habitName = ""
                for (habitSnapshot in dataSnapshot.children) {
                    val habit = habitSnapshot.getValue(HabitEntity::class.java)
                    if (habit != null && habit.id == habitId) {
                        habitName = habit.name
                        break
                    }
                }
                habitName
            }
    }
}