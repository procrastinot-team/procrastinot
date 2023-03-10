package com.github.mateo762.myapplication

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.mateo762.myapplication.fragments.*
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    
    private lateinit var drawer: DrawerLayout
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private lateinit var oneTapClient: SignInClient
    
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

        oneTapClient = Identity.getSignInClient(this)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            println("Already logged in")
            println(user.email)
            println(user.uid)
            println(FirebaseAuth.getInstance().uid)
        } else {
            println("Not logged in at all")
            createSignInIntent()
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            println("Sign in SUCCESSFULLY COMPLETED, user = $user")
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // and handle the error.
            val code = response!!.error!!.errorCode
            println("Code = $code")
        }
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                // ...
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