package com.github.mateo762.myapplication.home.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.post.Post
import com.github.mateo762.myapplication.ui.home.FeedScreen


class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            val feedPosts = ArrayList<Post>()
            // Fill with 5 sample posts generated locally for visualization / testing
            for (i in 1..5) {
                feedPosts.add(
                    Post(
                        "TEST_POST_$i",
                        "TEST_POST_DESCRIPTION_$i",
                        "@test_username",
                        "Associated habit to post $i",
                        // Pending image type / structure to handle, since we use test data at
                        // the moment, pass no image and use it directly to draw the post
                        null
                    )
                )
            }
            return ComposeView(requireContext()).apply {
                setContent {
                    FeedScreen(feedPosts)
                }
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreateView in FeedFragment", e)
            throw e
        }
    }
}