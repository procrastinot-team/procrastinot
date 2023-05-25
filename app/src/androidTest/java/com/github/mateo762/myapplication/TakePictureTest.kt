package com.github.mateo762.myapplication

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TakePictureTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TakePhotoActivity::class.java)


    @get:Rule
    var hiltRule = HiltAndroidRule(this)



    private lateinit var decorView: View
    private var habitsCollected: ArrayList<String> = ArrayList()
    private var habitsCoaches: ArrayList<String> = ArrayList()


    @get:Rule
    public val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)


    @Before
    fun setUp() {
        // use get activity
        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        var currentUser = "testUser"
        var firebaseUser = Firebase.auth.currentUser?.uid
        if (firebaseUser != null) {
            currentUser = firebaseUser.toString()
        }
        val db = Firebase.database.reference
        val ref =  db.child("users").child(currentUser).child("habitsPath")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                Log.d("TakePictureTest", "Habits exist")
                Log.d("TakePictureTest", it.toString())
                var children = it.children
                for (child in children) {
                    // write child value to object
                    var habitName = child.child("name").value.toString()
                    var coach = child.child("coach").value.toString()

                    habitsCollected += habitName
                    habitsCoaches += coach
                }
            }
        }
        Thread.sleep(500)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }
    

}
