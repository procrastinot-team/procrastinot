package com.github.mateo762.myapplication.coaching

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoachingActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(CoachingActivity::class.java)

    @Test
    fun testActivityLaunchesSuccessfully() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }
}