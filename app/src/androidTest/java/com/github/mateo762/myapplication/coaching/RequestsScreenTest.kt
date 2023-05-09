package com.github.mateo762.myapplication.coaching

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RequestsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var activityScenario: ActivityScenario<CoachingActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(CoachingActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun testRequestsPlaceholderTest() {
        composeTestRule
            .onNodeWithTag("placeholder_text")
            .assertExists()
            .assertIsDisplayed()
    }
}