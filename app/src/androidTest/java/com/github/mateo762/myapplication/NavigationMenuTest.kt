package com.github.mateo762.myapplication

import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.fragments.CalendarFragment
import com.github.mateo762.myapplication.fragments.PicturesFragment
import com.github.mateo762.myapplication.fragments.ProfileFragment
import com.github.mateo762.myapplication.fragments.SettingsFragment
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NavigationMenuTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun navigateToCalendarFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_calendar)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_calendar)).perform(click())

        // Get a reference to the current fragment
        val fragment = getCurrentFragment()

        // Check if the current fragment is a CalendarFragment
        assertTrue(fragment is CalendarFragment)
    }

    @Test
    fun navigateToProfileFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_profile)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_profile)).perform(click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()

        // Check if the current fragment is a CalendarFragment
        assertTrue(fragment is ProfileFragment)
    }

    @Test
    fun navigateToPicturesFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_pictures)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_pictures)).perform(click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()

        // Check if the current fragment is a CalendarFragment
        assertTrue(fragment is PicturesFragment)
    }

    @Test
    fun navigateToSettingsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_settings)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_settings)).perform(click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()

        // Check if the current fragment is a CalendarFragment
        assertTrue(fragment is SettingsFragment)
    }

    private fun getCurrentFragment(): Fragment? {
        // Get the current activity from the ActivityScenario
        var fragment: Fragment? = null
        activityScenario.onActivity { activity ->
            // Get the current fragment by its container ID
            fragment = activity.supportFragmentManager.findFragmentById(R.id.fragment_container)
        }
        return fragment
    }
}