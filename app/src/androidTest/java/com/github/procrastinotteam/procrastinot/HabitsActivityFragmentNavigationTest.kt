package com.github.procrastinotteam.procrastinot

import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.procrastinotteam.procrastinot.habits.HabitsActivity
import com.github.procrastinotteam.procrastinot.habits.fragments.DevelopFragment
import com.github.procrastinotteam.procrastinot.habits.fragments.WeekFragment
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HabitsActivityFragmentNavigationTest {
    private lateinit var activityScenario: ActivityScenario<HabitsActivity>

    @Before
    fun setUp() {
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
        // Check if the current fragment is a WeekFragment
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
        TestCase.assertTrue(fragment is DevelopFragment)
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