package com.github.mateo762.myapplication.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.models.HabitImage
import com.github.mateo762.myapplication.post.Post
import com.github.mateo762.myapplication.ui.home.FeedScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FeedFragment : Fragment() {
    private lateinit var imagesRef: DatabaseReference
    private val imagesState = mutableStateOf(emptyList<HabitImage>())

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FeedScreen(
                    posts = generatePosts(imagesState.value)
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase database reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            imagesRef =
                FirebaseDatabase.getInstance().getReference("/users/${currentUser.uid}/imagesPath")

            imagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fetchedImages = mutableListOf<HabitImage>()
                    for (childSnapshot in snapshot.children) {
                        val image = childSnapshot.getValue(HabitImage::class.java)
                        if (image != null) {
                            fetchedImages.add(image)
                        }
                    }
                    imagesState.value = fetchedImages
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "Error: ${error.message}")
                }
            })
        }
    }

    private fun generatePosts(fetchedImages: List<HabitImage>): ArrayList<Post> {
        val generatedPosts = ArrayList<Post>()
        for (i in fetchedImages) {
            generatedPosts.add(
                Post(
                    // Currently uploaded images have no caption/description or username
                    // TODO: add these fields upon image upload to generate a full Post
                    "TEST_POST_$i",
                    "TEST_POST_DESCRIPTION for Post with habitId ${i.habitId} uploaded on ${i.date}",
                    "@test_username",
                    "Post associated to habit ${i.habitId} ",
                    i
                )
            )
        }
        return generatedPosts
    }
}
