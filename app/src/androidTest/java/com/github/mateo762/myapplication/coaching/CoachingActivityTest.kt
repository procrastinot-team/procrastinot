package com.github.mateo762.myapplication.coaching

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CoachingActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(CoachingActivity.CoachingEntryPoint::class.java)

    @Test
    fun testActivityLaunchesSuccessfully() {
        activityScenarioRule.scenario.onActivity { activity ->
            //TODO
            //assertNotNull(activity)
        }
    }
}