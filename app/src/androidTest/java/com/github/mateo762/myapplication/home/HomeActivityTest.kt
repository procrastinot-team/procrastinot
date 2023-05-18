package com.github.mateo762.myapplication.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class HomeActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun activityLaunchesSuccessfully() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }
    @Test
    fun bottomNav_todayFragment_displaysTodayFragment() {
        onView(withId(R.id.todayFragment)).perform(click())

        assertEquals(TodayFragment::class.java, getCurrentFragment()?.javaClass)
    }

    @Test
    fun bottomNav_feedFragment_displaysFeedFragment() {
        onView(withId(R.id.feedFragment)).perform(click())

        assertEquals(FeedFragment::class.java, getCurrentFragment()?.javaClass)
    }

    @Test
    fun bottomNav_summaryFragment_displaysSummaryFragment() {
        onView(withId(R.id.summaryFragment)).perform(click())

        assertEquals(SummaryFragment::class.java, getCurrentFragment()?.javaClass)
    }

    private fun getCurrentFragment(): Fragment? {
        var currentFragment: Fragment? = null
        activityScenarioRule.scenario.onActivity { activity ->
            currentFragment = activity.supportFragmentManager.findFragmentById(R.id.navHostFragment)
        }
        return currentFragment
    }
}