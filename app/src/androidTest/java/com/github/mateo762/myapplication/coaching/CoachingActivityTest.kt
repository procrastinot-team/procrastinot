package com.github.mateo762.myapplication.coaching

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
<<<<<<< HEAD
import com.github.mateo762.myapplication.ui.coaching.OffersScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
=======
>>>>>>> parent of 19d5828 (Fixed CaochingActivityTest failing)
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class CoachingActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    val activityScenarioRule = ActivityScenarioRule(CoachingActivity::class.java)

    @Test
    fun testActivityLaunchesSuccessfully() {
        activityScenarioRule.scenario.onActivity { activity ->
            assertNotNull(activity)
        }
    }
}