package com.github.mateo762.myapplication

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.github.mateo762.myapplication.*
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.profile.ProfileActivity
import com.github.mateo762.myapplication.search.SearchActivity
import com.github.mateo762.myapplication.settings.SettingsActivity
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationActivityTest {

    private lateinit var activityScenario: ActivityScenario<HomeActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(HomeActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun navigateToHabitsActivity() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_habits)).perform(click())

        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()

        // Check if the current activity is a HabitsActivity
        assertTrue(currentActivity is HabitsActivity)
    }

    @Test
    fun navigateToSearchActivity() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_search)).perform(click())

        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()

        // Check if the current activity is a SearchActivity
        assertTrue(currentActivity is SearchActivity)
    }

    @Test
    fun navigateToProfileActivity() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_profile)).perform(click())

        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()

        // Check if the current activity is a ProfileActivity
        assertTrue(currentActivity is ProfileActivity)
    }

    @Test
    fun navigateToSettingsActivity() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_settings)).perform(click())

        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()

        // Check if the current activity is a ProfileActivity
        assertTrue(currentActivity is SettingsActivity)
    }


    // User icon tests
    @Test
    fun clickUserIconFromHome() {
        onView(withId(R.id.circle_imageView)).perform(click())
        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()
        // Check if the current activity is a SettingsActivity
        assertTrue(currentActivity is ProfileActivity)
    }

    @Test
    fun clickUserIconFromHabits() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_habits)).perform(click())
        onView(withId(R.id.circle_imageView)).perform(click())
        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()
        // Check if the current activity is a ProfileActivity
        assertTrue(currentActivity is ProfileActivity)
    }

    @Test
    fun clickUserIconFromSearch() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_search)).perform(click())
        onView(withId(R.id.circle_imageView)).perform(click())
        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()
        // Check if the current activity is a ProfileActivity
        assertTrue(currentActivity is ProfileActivity)
    }

    @Test
    fun clickUserIconFromSettings() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_settings)).perform(click())
        onView(withId(R.id.circle_imageView)).perform(click())
        // Get a reference to the current activity
        val currentActivity = getCurrentActivity()
        // Check if the current activity is a ProfileActivity
        assertTrue(currentActivity is ProfileActivity)
    }


    @Test
    fun navigateToHomeActivity() {
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_habits)).perform(click())
        // Go to another activity and return to check
        var currentActivity = getCurrentActivity()
        // Check if the current activity is a HomeActivity
        assertTrue(currentActivity is HabitsActivity)
        onView(withId(R.id.drawerLayout)).perform(DrawerActions.open())
        onView(withId(R.id.navView)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_home)).perform(click())
        currentActivity = getCurrentActivity()
        assertTrue(currentActivity is HomeActivity)
    }

    private fun getCurrentActivity(): Activity? {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync {
            run {
                currentActivity =
                    ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
                        Stage.RESUMED
                    ).elementAtOrNull(0)
            }
        }
        return currentActivity
    }

}