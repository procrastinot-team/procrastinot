package com.github.mateo762.myapplication

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mateo762.myapplication.fragments.CalendarFragment
import com.github.mateo762.myapplication.fragments.PicturesFragment
import com.github.mateo762.myapplication.fragments.ProfileFragment
import com.github.mateo762.myapplication.fragments.SettingsFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    lateinit var drawer: DrawerLayout

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
                CalendarFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.nav_calendar)
        }
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
            R.id.nav_calendar -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    CalendarFragment()
                ).commit()
            }
            R.id.nav_pictures -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    PicturesFragment()
                ).commit()
            }
            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    ProfileFragment()
                ).commit()
            }
            R.id.nav_settings -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    SettingsFragment()
                ).commit()
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Clicked share!", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_label -> {
                Toast.makeText(this, "Clicked label!", Toast.LENGTH_SHORT).show()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}