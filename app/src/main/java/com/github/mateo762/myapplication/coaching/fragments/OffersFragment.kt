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
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.OffersScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch

class OffersFragment : Fragment() {

    //Reference to the habits database
    private lateinit var habitsRef: DatabaseReference

    //Create an empty habit list state
    val habitsState = mutableStateOf(emptyList<HabitEntity>())

    // A list of habits where coaching has been requested
    private val coachingRequested =
        mutableStateOf(mutableListOf<HabitEntity>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                OffersScreen(coachingRequested.value, getCurrentUser().uid) { habit ->

                    habitsRef.orderByChild("id").equalTo(habit.id)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach { childSnapshot ->

                                    //Add the coach to the list of coaches for the habit
                                    updateHabitCoachesCallback(childSnapshot, habit, getCurrentUser())
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
                val habitsPath = "/habits"
                lifecycleScope.launch {
                    getFirebaseHabits(habitsPath)
                }
            }
        }
    }

    private fun getCurrentUser(): UserEntity{
        val firebaseEntity = FirebaseAuth.getInstance().currentUser
        return if (firebaseEntity == null) {
            UserEntity(uid="", name ="")
        } else {
            UserEntity(uid=firebaseEntity.uid, name =firebaseEntity.displayName)
        }
    }

    suspend fun getCoachableHabits() {
        val coachableHabitsState: MutableList<HabitEntity> = mutableListOf()
        for (coachableHabit in habitsState.value) {
            //Add all habits where coaching is requested and the habit is not yet coached and the habit is not owned by the current user
            if (coachableHabit.coachRequested && !coachableHabit.isCoached && coachableHabit.habitOwnerId != getCurrentUser().uid) {
                coachableHabitsState.add(coachableHabit)
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
                    println("Offers Screen: $snapshot")
                    val habit = childSnapshot.getValue(HabitEntity::class.java)
                    // BUG? snapshot.getValue forces isCoached to false, but not the other values?

                    if (habit != null) {
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

    /*
    Propagated function from the Offerscreen.
    The childSnapshot is the habit that will be appended with currentUser
    (the user who applied to coach for the given habit)
     */
    fun updateHabitCoachesCallback(
        childSnapshot: DataSnapshot,
        habit: HabitEntity,
        coach: UserEntity
    ) {
        val habitSnapshot = childSnapshot.getValue(HabitEntity::class.java)
        if (habitSnapshot != null && habitSnapshot.id == habit.id) {
            // Update the child with the matching ID
            val habitRef = childSnapshot.ref

            // Retrieve the current value of coachOffers
            habitRef.child("coachOffers").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val coachOffersList = mutableListOf<String>()

                    if (dataSnapshot.exists() && dataSnapshot.value != null) {
                        // If the coachOffers list already exists, retrieve its current value
                        val currentCoachOffers = dataSnapshot.getValue<List<String>>()
                        if (currentCoachOffers != null) {
                            coachOffersList.addAll(currentCoachOffers)
                        }
                    }

                    // Append the new coach to the list
                    coachOffersList.add(coach.uid) // Assuming the coach's name is stored in the 'name' property of UserEntity

                    // Update the coachOffers list in Firebase
                    habitRef.child("coachOffers").setValue(coachOffersList)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Update successful
                                println("Coach added to coachOffers list")
                            } else {
                                // Update failed
                                println("Failed to add coach to coachOffers list")
                            }
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    println("Error retrieving coachOffers: ${databaseError.message}")
                }
            })
        }
    }

}