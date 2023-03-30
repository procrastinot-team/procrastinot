package com.github.mateo762.myapplication.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : BaseActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        super.onCreateDrawer()
        bottomNavView = findViewById(R.id.bottomNav)
        bottomNavView.setOnItemSelectedListener(bottomNavListener)
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
}