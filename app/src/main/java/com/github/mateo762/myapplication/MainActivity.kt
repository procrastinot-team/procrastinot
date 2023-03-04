package com.github.mateo762.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.fragments.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                GreetFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.nav_greet)
        }

        val intent = Intent(this, CreateHabitActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_greet -> {
                openFragmentSelected(GreetFragment())
            }
            R.id.nav_calendar -> {
                openFragmentSelected(CalendarFragment())
            }
            R.id.nav_pictures -> {
                openFragmentSelected(PicturesFragment())
            }
            R.id.nav_profile -> {
                openFragmentSelected(ProfileFragment())
            }
            R.id.nav_settings -> {
                openFragmentSelected(SettingsFragment())
            }
            R.id.nav_share -> {
                showShortToastMessage("Clicked share!")
            }
            R.id.nav_label -> {
                showShortToastMessage("Clicked label!")
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openFragmentSelected(fragment: Fragment): Int {
        return supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, fragment
        ).commit()
    }

    private fun showShortToastMessage(message: String) {
        return Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}