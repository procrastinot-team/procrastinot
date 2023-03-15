package com.github.mateo762.myapplication.profile

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import com.github.mateo762.myapplication.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun onToolbarBackButtonClicked() {
        onView(withContentDescription("Navigate up")).perform(click())

        Thread.sleep(200)

        assertTrue(activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun onProfileGalleryTitleDisplayed() {
        onView(withId(R.id.profileGalleryTitle)).check(matches(withText(context.getString(R.string.profile_progress_gallery_title))))
    }
}