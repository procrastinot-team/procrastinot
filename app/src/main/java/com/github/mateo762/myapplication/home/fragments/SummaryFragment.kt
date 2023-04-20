package com.github.mateo762.myapplication.home.fragments

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.getHardCodedHabits
import com.github.mateo762.myapplication.ui.home.HabitListScreen


class SummaryFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        try {
            return ComposeView(requireContext()).apply {
                setContent {
                    HabitListScreen(habits = getHardCodedHabits())
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onCreateView in SummaryFragment", e)
            throw e
        }
    }
}
