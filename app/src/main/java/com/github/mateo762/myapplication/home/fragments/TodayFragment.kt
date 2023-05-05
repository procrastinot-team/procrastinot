package com.github.mateo762.myapplication.home.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mateo762.myapplication.habits.HabitsViewModel
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.room.*
import com.github.mateo762.myapplication.ui.home.TodayScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private lateinit var viewModel: HabitsViewModel

    @Inject
    lateinit var habitDao: HabitDao

    @Inject
    lateinit var habitImageDao: HabitImageDao

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var habitImageRepository: HabitImageRepository


    @RequiresApi(Build.VERSION_CODES.O)
    private val dateTime = LocalDateTime.of(2023, 4, 15, 17, 0)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TodayScreen(
                    time = viewModel.date,
                    habits = viewModel.habits.value,
                    images = viewModel.images.value
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HabitsViewModel::class.java)
    }


}



