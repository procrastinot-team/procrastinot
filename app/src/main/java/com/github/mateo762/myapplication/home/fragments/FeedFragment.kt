package com.github.mateo762.myapplication.home.fragments

import android.content.ContentValues
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.getHardCodedHabits
import com.github.mateo762.myapplication.ui.home.FeedScreen
import com.github.mateo762.myapplication.ui.home.HabitListScreen
import com.github.mateo762.myapplication.ui.home.UserCard

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            val feedImages : List<Image> = listOf()
            return ComposeView(requireContext()).apply {
                setContent {
                    FeedScreen(feedImages)
                }
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "onCreateView in FeedFragment", e)
            throw e
        }
    }
}
