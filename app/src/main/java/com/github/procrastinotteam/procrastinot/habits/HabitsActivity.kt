package com.github.procrastinotteam.procrastinot.habits

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.github.procrastinotteam.procrastinot.habits.fragments.WeekFragment

import com.github.procrastinotteam.procrastinot.BaseActivity
import com.github.procrastinotteam.procrastinot.R
import com.github.procrastinotteam.procrastinot.habits.fragments.DevelopFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                selectedFragment = DevelopFragment()
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