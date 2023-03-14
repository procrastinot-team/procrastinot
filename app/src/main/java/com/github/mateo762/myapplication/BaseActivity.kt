package com.github.mateo762.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.profile.ProfileActivity
import com.github.mateo762.myapplication.search.SearchActivity
import com.github.mateo762.myapplication.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView


open class BaseActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    protected fun onCreateDrawer() {
        drawer = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toggle =
            ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(navListener)
    }

    private fun openActivitySelected(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    private val navListener = NavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.nav_home -> {
                openActivitySelected(HomeActivity())
            }
            R.id.nav_habits -> {
                openActivitySelected(HabitsActivity())
            }
            R.id.nav_search -> {
                openActivitySelected(SearchActivity())
            }
            R.id.nav_profile -> {
                openActivitySelected(ProfileActivity())
            }
            R.id.nav_settings -> {
                openActivitySelected(SettingsActivity())
            }
        }
        true
    }


}