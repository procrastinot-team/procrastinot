package com.github.mateo762.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.habits.fragments.CreateHabitFragment
import com.github.mateo762.myapplication.habits.fragments.week.WeekFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HabitsActivityFragmentNavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var activityScenario: ActivityScenario<HabitsActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(HabitsActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun switchToWeekFragment() {
        Espresso.onView(withId(R.id.weekFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a com.github.mateo762.myapplication.habits.fragments.week.WeekFragment
        TestCase.assertTrue(fragment is WeekFragment)
    }

    @Test
    fun switchToSummaryFragment() {
        Espresso.onView(withId(R.id.listFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a ListFragment
        TestCase.assertTrue(fragment is ListFragment)
    }

    @Test
    fun switchToDevelopFragment() {
        Espresso.onView(withId(R.id.weekFragment)).perform(ViewActions.click())
        // Go to weekFragment and back to test DevelopFragment
        Espresso.onView(withId(R.id.developFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a DevelopFragment
        TestCase.assertTrue(fragment is CreateHabitFragment)
    }

    private fun getCurrentFragment(): Fragment? {
        // Get the current fragment from the ActivityScenario
        var fragment: Fragment? = null
        activityScenario.onActivity { activity ->
            // Get the current fragment by its container ID
            fragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragment)
        }
        return fragment
    }
}