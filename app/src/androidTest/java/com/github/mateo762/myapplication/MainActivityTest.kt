package com.github.mateo762.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

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
    fun testIntentSentOnButtonClick() {
        // Click the "mainGreetButton" button
        onView(withId(R.id.mainGreetButton)).perform(click())

        // Check that an intent was sent with the "name" attribute
        intended(
            allOf(
                hasExtraWithKey("name"),
                hasComponent(GreetingActivity::class.java.name)
            )
        )
    }
}