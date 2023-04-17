package com.github.mateo762.myapplication.feed

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedFragmentTest {
    private lateinit var activityScenario: ActivityScenario<HomeActivity>

    @Before
    fun setUp() {
        // Travel to Feed Fragment
        activityScenario = ActivityScenario.launch(HomeActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.feedFragment)).perform(ViewActions.click())
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testThumbnailVisible(){

    }
}


// FeedFragment Tests
// Test the thumbnail elements visible
// Test the transition to PostActivity and back
// Test scrolling of the feedFragment

// Create Firebase bytes to image adapter