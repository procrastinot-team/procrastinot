package com.github.mateo762.myapplication.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.habits.fragments.CreateHabitFragment
import com.github.mateo762.myapplication.habits.fragments.week.WeekFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class HabitsActivity : BaseActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits)
        super.onCreateDrawer()
        bottomNavView = findViewById(R.id.bottomNav)
        bottomNavView.setOnItemSelectedListener(bottomNavListener)
    }

    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener {
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.developFragment -> {
                selectedFragment = CreateHabitFragment()
            }
            R.id.weekFragment -> {
                selectedFragment = WeekFragment()
            }
            R.id.listFragment -> {
                selectedFragment = ListFragment()
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
}