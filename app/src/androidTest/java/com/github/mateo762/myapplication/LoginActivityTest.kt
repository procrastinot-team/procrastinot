package com.github.mateo762.myapplication

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.login.LoginActivity
import com.github.mateo762.myapplication.register.RegisterActivity
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private var email = "user@gmail.com"
    private var password = "12345678"
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        // Initialize the Intents framework
        Intents.init()
    }

    @After
    fun close() {
        // Release the Intents framework
        Intents.release()
    }

    @Test
    fun testLogin() {
        composeTestRule
            .onNodeWithTag("btn_not_registered")
            .performClick()
        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(RegisterActivity::class.java.name)
            )
        )
    }

    @Test
    fun testLoginAfterTyping() {
        // click
        composeTestRule
            .onNodeWithTag("text_email")
            .performClick()
            .performTextInput(email)
        composeTestRule
            .onNodeWithTag("text_password")
            .performClick()
            .performTextInput(password)
        Espresso.closeSoftKeyboard()
        composeTestRule
            .onNodeWithTag("btn_not_registered")
            .performClick()
        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(RegisterActivity::class.java.name)
            )
        )
    }
}