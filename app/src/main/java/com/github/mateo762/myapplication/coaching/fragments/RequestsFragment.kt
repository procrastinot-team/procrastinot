package com.github.mateo762.myapplication.coaching.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.RequestsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val testCoachableHabitList = mutableListOf<HabitEntity>()
        val candidateCoaches = mutableListOf<String>()
        candidateCoaches.add("coach_username")
        candidateCoaches.add("second_username")
        val habit1 = HabitEntity("0", "Running", emptyList(), "9:00", "12:00",
            false, true, candidateCoaches,"")
        val habit2 = HabitEntity("1", "Studying", emptyList(), "14:00", "16:00",
            false, true, candidateCoaches,"")
        testCoachableHabitList.add(habit1)
        testCoachableHabitList.add(habit2)
        return ComposeView(requireContext()).apply {
            setContent {
                RequestsScreen(testCoachableHabitList)
            }
        }
    }

}