package com.github.mateo762.myapplication

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.fragments.*
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class GreetingFragmentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        // Initialize the Intents framework
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release the Intents framework
        Intents.release()
    }

    @Test
    fun clickGreetButtonSendsIntentGreetingActivity() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_greet)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_greet)).perform(click())
        // click on the plain text and write "John"
        composeTestRule.onNodeWithTag("text_name").performClick().performTextInput("John")
        composeTestRule.onNodeWithTag("btn_greet").performClick()
        intended(
            allOf(
                hasExtra("name", "John"),
                hasComponent(GreetingActivity::class.java.name)
            )
        )
    }
}