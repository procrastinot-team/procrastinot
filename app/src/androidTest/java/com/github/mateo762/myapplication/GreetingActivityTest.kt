package com.github.mateo762.myapplication;

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(GreetingActivity::class.java)

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
    fun testGreetingActivity() {
        // Create an Intent to launch the greeting activity
        val intent = Intent(ApplicationProvider.getApplicationContext(), GreetingActivity::class.java)
        intent.putExtra("name", "John")

        // Launch the activity with the intent and obtain a reference to the activity scenario
        val scenario = ActivityScenario.launch<GreetingActivity>(intent)

        // Use Espresso Intents to check that the activity received an intent with the correct action
        intended(hasExtraWithKey("name"))

        onView(withId(R.id.greetingText)).check(matches(withText("Hello John!")))
        // Close the activity scenario
        scenario.close()
    }
}
