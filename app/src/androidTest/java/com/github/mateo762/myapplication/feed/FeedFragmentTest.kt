package com.github.mateo762.myapplication.feed

import android.app.Activity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.post.PostActivity
import com.github.mateo762.myapplication.models.HabitImageEntity
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
class FeedFragmentTest {
    private lateinit var activityScenario: ActivityScenario<HomeActivity.HomeEntryPoint>
    private var imagesRef = ArrayList<HabitImageEntity>()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        // Travel to Feed Fragment
        activityScenario = ActivityScenario.launch(HomeActivity.HomeEntryPoint::class.java)
        fillTestPosts()
        Espresso.onView(ViewMatchers.withId(R.id.feedFragment)).perform(ViewActions.click())
    }

    private fun fillTestPosts() {
        for (i in 1..5) {
            imagesRef.add((HabitImageEntity("id","TEST_ID_$i", "TEST_URL_$i", "TEST_DATE_$i")))
        }

    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testThumbnailVisible() {
        composeTestRule.onAllNodes(hasTestTag("post_thumbnail")).apply {
            fetchSemanticsNodes().forEachIndexed { i, _ ->
                get(i).performScrollTo().assertExists().assertIsDisplayed()
            }
        }
    }

    @Test
    fun testPostTraversal() {
        composeTestRule.onAllNodes(hasTestTag("post_thumbnail")).apply {
            fetchSemanticsNodes().forEachIndexed { i, _ ->
                get(i).performScrollTo().performClick()
                getInstrumentation().waitForIdleSync()
                // Get a reference to the current activity
                var currentActivity = getCurrentActivity()
                getInstrumentation().waitForIdleSync()
                // Check if the current activity is a PostActivity
                TestCase.assertTrue(currentActivity is PostActivity)
                // The post contents passed are tested in PostActivityTest
                Espresso.pressBack()
                getInstrumentation().waitForIdleSync()
                // Get a reference to the current activity
                currentActivity = getCurrentActivity()
                // Check if the current activity is a HomeActivity
                TestCase.assertTrue(currentActivity is HomeActivity.HomeEntryPoint)
                // Get a reference to the current fragment
                val fragment = getCurrentFragment()
                // Check if the current fragment is a FeedFragment
                TestCase.assertTrue(fragment is FeedFragment)
            }
        }
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


