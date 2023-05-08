package com.github.mateo762.myapplication.home.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.room.PostDao
import com.github.mateo762.myapplication.room.PostRepository
import com.github.mateo762.myapplication.ui.home.FeedScreen
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private lateinit var usersRef: DatabaseReference
    private lateinit var imagesRef: DatabaseReference
    private val feedState = mutableStateOf(emptyList<PostEntity>())

    @Inject
    lateinit var postDao: PostDao

    @Inject
    lateinit var postRepository: PostRepository

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FeedScreen(posts = feedState.value)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the connectivityManager to verify network functions
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        // Verify we have connection -- this way we will at least always run the Listener,
        // and if Firebase fails, then we run the failed action onCancelled
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val connectionExists =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

        // Connect to Firebase for real time data
        // Initialize Firebase database reference
        if (connectionExists) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                usersRef = FirebaseDatabase.getInstance().getReference("/users/${currentUser.uid}")
                fetchFollowingUsers()
            }
        } else {
            // There is no connection available - (plane mode, no service, wifi...) Use cached data
            // The Firebase Listener never runs if there is no connection!
            getLocalPosts()
            Toast.makeText(context, "You're offline, using cached data", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchFollowingUsers() {
        usersRef.child("followingPath").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val followingUserList =
                        childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                    if (followingUserList != null) {
                        for (followingUserId in followingUserList) {
                            Log.d(TAG, "hey: $followingUserId")
                            fetchUserImages(followingUserId)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Error: ${error.message}")
                getLocalPosts()
                Toast.makeText(
                    context,
                    "Can't reach the server, using cached data",
                    Toast.LENGTH_LONG
                ).show()
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
            val generatedPosts = mutableListOf<PostEntity>()

            fetchedImages.forEach { image ->
                val username = fetchUsernameForImage(image.userId).await()
                val habitName = fetchHabitNameForImage(image.userId, image.habitId).await()
                val postEntity = PostEntity(
                    0,
                    "TEST_POST_CAPTION for PostEntity relating to ${image.habitId}",
                    "TEST_POST_DESCRIPTION for Post with habitId ${image.habitId} uploaded on ${image.date}",
                    "Posted on ${image.date}",
                    image.url,
                    username,
                    "Post associated to habit ${image.habitId}",
                    image.id
                )
                generatedPosts.add(postEntity)
            }

            withContext(Dispatchers.Main) {
                feedState.value = feedState.value + generatedPosts
                updatePostsCache(feedState.value)
            }
        }
    }

    private fun updatePostsCache(feedState: List<PostEntity>) {
        GlobalScope.launch {
            postRepository.insertAllPosts(feedState)
        }
    }

    fun getLocalPosts() {
        GlobalScope.launch {
            feedState.value = postRepository.getAllPosts()
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