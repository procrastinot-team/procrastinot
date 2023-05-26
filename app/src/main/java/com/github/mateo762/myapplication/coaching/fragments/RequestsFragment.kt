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
import kotlinx.coroutines.launch

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

    /*
    This method is a propagated callback from the @Composable in the RequestsScreen UI
    component. The habit and coach are taken from the selection done by the user via
    the button and the childSnapshot is the corresponding Firebase entity that will
    be updated to reflect the user's choice.
     */
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
                    "coach" to coach.uid
                )
            )

            val sharedHabitUrl = habit.sharedHabitUrl
            if (sharedHabitUrl != "") {
                val ref = FirebaseDatabase.getInstance().getReferenceFromUrl(sharedHabitUrl)

                ref.removeValue()
                    .addOnSuccessListener {
                        // Deletion successful
                    }
                    .addOnFailureListener { error ->
                        // Handle deletion failure
                        println("Failed to delete shared habit: $error")
                    }
            }

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
                            habit.isCoached =
                                childSnapshot.child("isCoached").getValue(Boolean::class.java)
                                    ?: false
                            // Otherwise, set it to false
                        } else {
                            habit.isCoached = false
                            habit.coachOffers = emptyList()

                            getCoachOffersFromFirebase(habit) { coachOffers ->
                                habit.coachOffers = coachOffers

                                //Update the UI
                                lifecycleScope.launch {
                                    habitsState.value = coachableHabits
                                    getCoachableAndCoachedHabits()
                                }
                            }
                        }

                        coachableHabits.add(habit)
                    }
                }
                lifecycleScope.launch {
                    habitsState.value = coachableHabits
                    getCoachableAndCoachedHabits()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /*
    Retrieves the list of coaches from the Firebase database from the /habits/{habit.id}/coachOffers path and returns it as a list of UserEntity objects
     */
    private fun getCoachOffersFromFirebase(
        habit: HabitEntity,
        callback: (List<String>) -> Unit
    ) {
        println("Calling getCoachOffersFromFirebase with habit: $habit")

        //Concatenate the habit.sharedHabitUrl with the /coachOffers path
        val sharedHabitUrl = habit.sharedHabitUrl

        if (sharedHabitUrl != "") {
            val url = "${habit.sharedHabitUrl}/coachOffers"

            val coachOffersRef = FirebaseDatabase.getInstance().getReferenceFromUrl(url)
            coachOffersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val coachOffers = mutableListOf<String>()
                    for (childSnapshot in snapshot.children) {
                        val coach = childSnapshot.getValue(String::class.java)
                        if (coach != null) {
                            coachOffers.add(coach)
                        }
                    }

                    callback(coachOffers.toList())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    callback(emptyList())
                }
            })
        }
    }

}
