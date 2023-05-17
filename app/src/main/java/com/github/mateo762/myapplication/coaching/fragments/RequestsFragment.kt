package com.github.mateo762.myapplication.coaching.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.ui.coaching.RequestsScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("MutableCollectionMutableState")
class RequestsFragment : Fragment() {

    private lateinit var habitsRef: DatabaseReference
    val habitsState = mutableStateOf(emptyList<HabitEntity>())

    // A list with each habit with coachRequested and !isCoached, with the coaches offered
    private val coachableHabits =
        mutableStateOf(mutableListOf<Map<HabitEntity, List<UserEntity>>>())

    // A list with each habit with isCoached, with the coach selected
    private val coachedHabits = mutableStateOf(mutableListOf<Map<HabitEntity, UserEntity>>())

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RequestsScreen(coachableHabits.value, coachedHabits.value) { coach, habit ->
                    habitsRef.orderByChild("id").equalTo(habit.id)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach { childSnapshot ->
                                    updateCoachStateCallback(childSnapshot, habit, coach)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }
        }
    }

    fun updateCoachStateCallback(
        childSnapshot: DataSnapshot,
        habit: HabitEntity,
        coach: UserEntity
    ) {
        val habitSnapshot = childSnapshot.getValue(HabitEntity::class.java)
        if (habitSnapshot != null && !habitSnapshot.isCoached && habitSnapshot.id == habit.id) {
            // Update the child with the matching ID
            childSnapshot.ref.updateChildren(
                mapOf(
                    "isCoached" to true,
                    "coach" to coach.uid))
            val currentUser = FirebaseAuth.getInstance().currentUser
            getFirebaseCoachableHabitsFromPath("/users/${currentUser?.uid}/habitsPath")
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
                lifecycleScope.launch {
                    getFirebaseCoachableHabitsFromPath(habitsPath)
                }
            }
        }
    }

    suspend fun getCoachableAndCoachedHabits() {
        val coachedHabitsState: MutableList<Map<HabitEntity, UserEntity>> = mutableListOf()
        val coachableHabitsState: MutableList<Map<HabitEntity, List<UserEntity>>> = mutableListOf()
        for (coachableHabit in habitsState.value) {
            if (coachableHabit.isCoached) {
                val coach = UserRepository().getUser(coachableHabit.coach)
                if (coach != null) {
                    coachedHabitsState.add(mapOf(coachableHabit to coach))
                }
            } else {
                val requestedCandidates =
                    coachableHabit.coachOffers.mapNotNull { UserRepository().getUser(it) }
                coachableHabitsState.add(mapOf(coachableHabit to requestedCandidates))
            }
        }
        this@RequestsFragment.coachedHabits.value = coachedHabitsState
        this@RequestsFragment.coachableHabits.value = coachableHabitsState

    }

    private fun getFirebaseCoachableHabitsFromPath(path: String) {
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
                            // Otherwise, set it to false
                        } else { habit.isCoached = false }
                        coachableHabits.add(habit) }
                }
                lifecycleScope.launch {
                    habitsState.value = coachableHabits
                    getCoachableAndCoachedHabits()
                } }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
