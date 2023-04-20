package com.github.mateo762.myapplication

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mateo762.myapplication.authentication.LoginActivity
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.profile.ProfileActivity
import com.github.mateo762.myapplication.search.SearchActivity
import com.github.mateo762.myapplication.settings.SettingsActivity
import com.github.mateo762.myapplication.upload_gallery.UploadPictureActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    protected fun onCreateDrawer() {
        drawer = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        // The title will be set statically per activity on their individual style
        toolbar.title = ""
        setSupportActionBar(toolbar)
        val toggle =
            ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(navListener)
        // Enable clicking the profile picture -- go to profile activity
        val profileIcon: ImageView = findViewById(R.id.circle_imageView)
        profileIcon.setOnClickListener { openActivitySelected(ProfileActivity()) }
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
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_settings -> {
                openActivitySelected(SettingsActivity())
            }
            R.id.nav_log_out -> {
                val intent = Intent(this@BaseActivity, LoginActivity::class.java)
                FirebaseAuth.getInstance().signOut()
                startActivity(intent)
            }
        }
        true
    }


}