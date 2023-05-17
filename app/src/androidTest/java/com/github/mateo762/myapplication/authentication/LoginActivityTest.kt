package com.github.mateo762.myapplication.authentication

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.ToastMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class LoginActivityTest {
    private val invalidEmail = "user.gmail.com"
    private val validEmail = "user@gmail.com"
    private val validPassword = "12345678"
    private val invalidPassword = "1234"

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
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
            .performTextInput(validEmail)
        composeTestRule
            .onNodeWithTag("text_password")
            .performClick()
            .performTextInput(validPassword)
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
    @Test
    fun testBtnLoginInvalidEmail() {
        // click
        composeTestRule
            .onNodeWithTag("text_email")
            .performClick()
            .performTextInput(invalidEmail)
        composeTestRule
            .onNodeWithTag("text_password")
            .performClick()
            .performTextInput(validPassword)
        Espresso.closeSoftKeyboard()
        composeTestRule
            .onNodeWithTag("btn_login")
            .performClick()

        Espresso.onView(ViewMatchers.withText(Matchers.startsWith("email is badly formatted")))
            .inRoot(ToastMatcher().apply {
                matches(ViewMatchers.isDisplayed())
            })
    }

    @Test
    fun testBtnLoginInvalidEmailAndPassword() {
        // click
        composeTestRule
            .onNodeWithTag("text_email")
            .performClick()
            .performTextInput(invalidEmail)
        composeTestRule
            .onNodeWithTag("text_password")
            .performClick()
            .performTextInput(invalidPassword)
        Espresso.closeSoftKeyboard()
        composeTestRule
            .onNodeWithTag("btn_login")
            .performClick()

        Espresso.onView(ViewMatchers.withText(Matchers.startsWith("email is badly formatted")))
            .inRoot(ToastMatcher().apply {
                matches(ViewMatchers.isDisplayed())
            })
    }

    @Test
    fun testBtnLoginInvalidPassword() {
        // click
        composeTestRule
            .onNodeWithTag("text_email")
            .performClick()
            .performTextInput(validEmail)
        composeTestRule
            .onNodeWithTag("text_password")
            .performClick()
            .performTextInput(invalidPassword)
        Espresso.closeSoftKeyboard()
        composeTestRule
            .onNodeWithTag("btn_login")
            .performClick()

        Espresso.onView(ViewMatchers.withText(Matchers.startsWith("The given password is invalid.")))
            .inRoot(ToastMatcher().apply {
                matches(ViewMatchers.isDisplayed())
            })
    }
    @Test
    fun testBtnLoginEmptyValues() {
        // click
        composeTestRule
            .onNodeWithTag("btn_login")
            .performClick()
        Espresso.onView(ViewMatchers.withText(R.string.error_empty_login))
            .inRoot(ToastMatcher().apply {
                matches(ViewMatchers.isDisplayed())
            })
    }


    @Test
    fun testBtnLogin() {
        // click
        composeTestRule
            .onNodeWithTag("text_email")
            .performClick()
            .performTextInput(validEmail)
        composeTestRule
            .onNodeWithTag("text_password")
            .performClick()
            .performTextInput(validPassword)
        Espresso.closeSoftKeyboard()
        composeTestRule
            .onNodeWithTag("btn_login")
            .performClick()

        // TODO - mock auth to test successfully login
//        onView(withText(com.github.mateo762.myapplication.R.string.success_login))
//            .inRoot(ToastMatcher().apply {
//                matches(isDisplayed())
//            })
//        Intents.intended(
//            allOf(
//                IntentMatchers.hasComponent(BaseActivity::class.java.name)
//            )
//        )
    }
}