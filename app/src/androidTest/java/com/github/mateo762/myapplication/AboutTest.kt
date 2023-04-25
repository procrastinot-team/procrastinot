package com.github.mateo762.myapplication

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.about.AboutActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AboutTest {

    private var user_count: String = "Loading..."
    private var posts_count: String = "Loading..."
    private var completed_count: String = "Loading..."

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(AboutActivity::class.java)


    @Before
    fun setUp() {
        hiltRule.inject()

        val db = Firebase.database.reference
        db.child("usernames").get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            user_count = it.childrenCount.toString()
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            user_count = "Null"
        }

        db.child("users").get().addOnSuccessListener {
            val users = it.children // get all users
            var postsCount = 0
            var complCount = 0
            for (user in users) {
                // get all habits for each user in habitsPath
                val habitsPath = user.child("habitsPath").value.toString()
                if (habitsPath != "null") {
                    val habits = user.child("habitsPath").childrenCount
                    postsCount += habits.toInt()
                }
                val compl = user.child("imagesPath").value.toString()
                if (compl != "null") {
                    val compls = user.child("imagesPath").childrenCount
                    complCount += compls.toInt()
                }
            }
            posts_count = postsCount.toString()
            completed_count = complCount.toString()
        }.addOnFailureListener {
        }
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testUserCount() {
        // get the data from text field and compare it with the data from firebase
        Thread.sleep(1000)
        Log.d("Test", "testAbout: $user_count, $posts_count, $completed_count")
        onView(withId(R.id.users)).check(matches(withText(user_count)))
    }

    @Test
    fun testPostsCount() {
        // get the data from text field and compare it with the data from firebase
        Thread.sleep(1000)
        Log.d("Test", "testAbout: $user_count, $posts_count, $completed_count")
        onView(withId(R.id.posts)).check(matches(withText(posts_count)))
    }

    @Test
    fun testCompletedCount() {
        // get the data from text field and compare it with the data from firebase
        Thread.sleep(1000)
        Log.d("Test", "testAbout: $user_count, $posts_count, $completed_count")
        onView(withId(R.id.compl)).check(matches(withText(completed_count)))
    }


}