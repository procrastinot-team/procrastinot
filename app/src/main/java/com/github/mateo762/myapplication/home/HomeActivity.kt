package com.github.mateo762.myapplication.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.habits.HabitsViewModel
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.room.HabitDao
import com.github.mateo762.myapplication.room.HabitImageDao
import com.github.mateo762.myapplication.room.HabitImageRepository
import com.github.mateo762.myapplication.room.HabitRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private val habitsViewModel: HabitsViewModel by viewModels()

    private lateinit var imagesRef: DatabaseReference
    private val imagesState = mutableStateOf(emptyList<HabitImageEntity>())
    private lateinit var habitsRef: DatabaseReference
    private val habitsState = mutableStateOf(emptyList<HabitEntity>())
    private val TAG = TodayFragment::class.java.simpleName

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        super.onCreateDrawer()
        bottomNavView = findViewById(R.id.bottomNav)
        bottomNavView.setOnItemSelectedListener(bottomNavListener)

        //TODO: Retrieve the habits here and send to fragments
        habitsViewModel.data.value
    }

    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener {
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.todayFragment -> {
                selectedFragment = TodayFragment()
            }
            R.id.feedFragment -> {
                selectedFragment = FeedFragment()
            }
            R.id.summaryFragment -> {
                selectedFragment = SummaryFragment()
            }
        }
        openFragmentSelected(selectedFragment)
        true
    }

    private fun openFragmentSelected(fragment: Fragment): Int {
        return supportFragmentManager.beginTransaction().replace(
            R.id.navHostFragment, fragment
        ).commit()
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
                Toast.makeText(this@HomeActivity,
                    "Can't reach the server, using cached data",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun getFirebaseHabitImagesFromPath(path: String) {
        imagesRef = FirebaseDatabase.getInstance()
            .getReference(path)

        imagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedImages = mutableListOf<HabitImageEntity>()
                for (childSnapshot in snapshot.children) {
                    val image = childSnapshot.getValue(HabitImageEntity::class.java)
                    if (image != null) {
                        fetchedImages.add(image)
                    }
                }
                imagesState.value = fetchedImages
                updateImagesCache(fetchedImages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Error: ${error.message}")
                // Fetch local images stored
                getLocalImages()
                Toast.makeText(
                    this@HomeActivity,
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

    private fun getLocalImages() {
        GlobalScope.launch {
            imagesState.value = habitImageRepository.getAllHabitImages()
        }
    }


    fun updateHabitsCache(fetchedHabits: MutableList<HabitEntity>) {
        GlobalScope.launch {
            habitRepository.insertAllHabits(fetchedHabits)
        }
    }

    private fun updateImagesCache(fetchedImages: MutableList<HabitImageEntity>) {
        GlobalScope.launch {
            habitImageRepository.insertAllHabitImages(fetchedImages)
        }
    }

}