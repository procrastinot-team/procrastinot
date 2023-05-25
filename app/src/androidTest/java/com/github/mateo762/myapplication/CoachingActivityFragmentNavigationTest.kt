package com.github.mateo762.myapplication

import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.coaching.CoachingActivity
import com.github.mateo762.myapplication.coaching.fragments.OffersFragment
import com.github.mateo762.myapplication.coaching.fragments.RequestsFragment
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
class CoachingActivityFragmentNavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

   private lateinit var activityScenario: ActivityScenario<CoachingActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
        activityScenario = ActivityScenario.launch(CoachingActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun switchToOffersFragment() {
        Espresso.onView(withId(R.id.offersFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a FeedFragment
        TestCase.assertTrue(fragment is OffersFragment)
    }


    @Test
    fun switchToRequestsFragment() {
        Espresso.onView(withId(R.id.offersFragment)).perform(ViewActions.click())
        // Go to feedFragment and back to test todayFragment
        Espresso.onView(withId(R.id.requestsFragment)).perform(ViewActions.click())
        // Get a reference to the current fragment
        val fragment = getCurrentFragment()
        // Check if the current fragment is a TodayFragment
        TestCase.assertTrue(fragment is RequestsFragment)
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