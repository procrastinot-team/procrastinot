package com.github.mateo762.myapplication.home.fragments

import android.content.ContentValues.TAG
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
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.getHardCodedHabits
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.room.HabitRepository
import com.github.mateo762.myapplication.ui.home.HabitListScreen
import com.github.mateo762.myapplication.ui.home.TodayScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class SummaryFragment : Fragment() {

    private lateinit var habitsRef: DatabaseReference
    private val habitsState = mutableStateOf(emptyList<HabitEntity>())
    private val TAG = TodayFragment::class.java.simpleName

    @Inject
    lateinit var habitRepository: HabitRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HabitListScreen(
                    habits = habitsState.value
                )
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
        if (connectionExists) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val habitsPath = "/users/${currentUser.uid}/habitsPath"
                getFirebaseHabitsFromPath(habitsPath)
            }
        } else {
            // There is no connection available - (plane mode, no service, wifi...) Use cached data
            // The Firebase Listener never runs if there is no connection!
            getLocalHabits()
            Toast.makeText(context, "You're offline, using cached data", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFirebaseHabitsFromPath(path: String) {
        // Initialize Firebase database reference
        habitsRef = FirebaseDatabase.getInstance().getReference(path)
        habitsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedHabits = mutableListOf<HabitEntity>()
                for (childSnapshot in snapshot.children) {
                    val habit = childSnapshot.getValue(HabitEntity::class.java)
                    if (habit != null) {
                        fetchedHabits.add(habit)
                    }
                }
                habitsState.value = fetchedHabits
                updateHabitsCache(fetchedHabits)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Error: ${error.message}")
                // We have connection, but fetching from Firebase failed
                // Fetch local data stored
                getLocalHabits()
                Toast.makeText(
                    context,
                    "Can't reach the server, using cached data",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun getLocalHabits() {
        GlobalScope.launch {
            habitsState.value = habitRepository.getAllHabits()
        }
    }

    fun updateHabitsCache(fetchedHabits: MutableList<HabitEntity>) {
        GlobalScope.launch {
            habitRepository.insertAllHabits(fetchedHabits)
        }
    }

}

