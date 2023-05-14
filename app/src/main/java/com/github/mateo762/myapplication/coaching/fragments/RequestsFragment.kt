package com.github.mateo762.myapplication.coaching.fragments

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
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.room.HabitRepository
import com.github.mateo762.myapplication.ui.coaching.RequestsScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RequestsFragment : Fragment() {

    private lateinit var habitsRef: DatabaseReference
    private val habitsState = mutableStateOf(emptyList<HabitEntity>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RequestsScreen(habitsState.value)
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
            Toast.makeText(context, "You're offline", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFirebaseHabitsFromPath(path: String) {
        // Initialize Firebase database reference
        habitsRef = FirebaseDatabase.getInstance().getReference(path)
        habitsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val coachableHabits = mutableListOf<HabitEntity>()
                for (childSnapshot in snapshot.children) {
                    val habit = childSnapshot.getValue(HabitEntity::class.java)
                    // BUG? snapshot.getValue forces isCoached to false, but not the other values?
                    if (habit != null && habit.coachRequested) {
                        if (childSnapshot.hasChild("isCoached")) {
                            // If the snapshot contains a value for isCoached, use it
                            habit.isCoached = childSnapshot.child("isCoached").getValue(Boolean::class.java) ?: false
                        } else {
                            // Otherwise, set it to false
                            habit.isCoached = false
                        }
                        coachableHabits.add(habit)
                    }
                }
                habitsState.value = coachableHabits
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
