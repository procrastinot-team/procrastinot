package com.github.mateo762.myapplication.coaching.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.OffersScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class OffersFragment : Fragment() {

    //Reference to the habits database
    private lateinit var habitsRef: DatabaseReference

    //Create an empty habit list state
    val habitsState = mutableStateOf(emptyList<HabitEntity>())

    // A list of habits where coaching has been requested
    private val coachingRequested =
        mutableStateOf(mutableListOf<Map<HabitEntity, UserEntity>>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                OffersScreen(coachingRequested.value) { coachee, habit ->
                    habitsRef.orderByChild("id").equalTo(habit.id)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach { childSnapshot ->

                                    //Add the coach to the list of coaches for the habit
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
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
                val coachingHabits = "coachingHabits"

                //TODO: Create a "coachable habits" public path on firebase and refactor the habit storing methods

                lifecycleScope.launch {
                    getFirebaseHabits(habitsPath)
                }
            }
        }
    }

    private fun getCurrentUser(): UserEntity{
        return UserEntity("currentUserId", "getCurrentUser", "coachee_user", "coachee_user@email.com")
    }

    suspend fun getCoachableHabits() {
        val coachableHabitsState: MutableList<Map<HabitEntity, UserEntity>> = mutableListOf()
        for (coachableHabit in habitsState.value) {
            //Add all habits where coaching is requested and the habit is not yet coached
            if (coachableHabit.coachRequested && !coachableHabit.isCoached) {
                coachableHabitsState.add(mapOf(coachableHabit to getCurrentUser()))
            }
            this@OffersFragment.coachingRequested.value = coachableHabitsState
        }
    }

    //Retrieve the list of all habits from firebase
    private fun getFirebaseHabits(path: String) {
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
                            habit.isCoached =
                                childSnapshot.child("isCoached").getValue(Boolean::class.java)
                                    ?: false
                            // Otherwise, set it to false
                        } else {
                            habit.isCoached = false
                        }
                        coachableHabits.add(habit)
                    }
                }
                lifecycleScope.launch {
                    habitsState.value = coachableHabits
                    getCoachableHabits()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    
}