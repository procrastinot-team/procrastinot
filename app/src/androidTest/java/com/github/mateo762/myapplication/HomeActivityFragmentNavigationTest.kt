package com.github.mateo762.myapplication

import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityFragmentNavigationTest {
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
    fun switchToFeedFragment() {
        Espresso.onView(withId(R.id.feedFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a FeedFragment
        TestCase.assertTrue(fragment is FeedFragment)
    }

    @Test
    fun switchToSummaryFragment() {
        Espresso.onView(withId(R.id.summaryFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a SummaryFragment
        TestCase.assertTrue(fragment is SummaryFragment)
    }

    @Test
    fun switchToHomeFragment() {
        Espresso.onView(withId(R.id.feedFragment)).perform(ViewActions.click())
        // Go to feedFragment and back to test todayFragment
        Espresso.onView(withId(R.id.todayFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a TodayFragment
        TestCase.assertTrue(fragment is TodayFragment)
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