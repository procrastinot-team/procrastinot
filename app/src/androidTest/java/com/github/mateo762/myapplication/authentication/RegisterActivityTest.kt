package com.github.mateo762.myapplication.authentication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.ToastMatcher
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {
    /*

    private val name = "Louca"
    private val surname = "Zacharie"
    private val invalidEmail = "user.gmail.com"
    private val validEmail = "user@gmail.com"
    private val validPassword = "12345678"
    private val invalidPassword = "1234"

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

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
            .onNodeWithTag("btn_already_login")
            .performClick()
        Intents.intended(
            allOf(
                IntentMatchers.hasComponent(LoginActivity::class.java.name)
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
            .onNodeWithTag("btn_already_login")
            .performClick()
        Intents.intended(
            allOf(
                IntentMatchers.hasComponent(LoginActivity::class.java.name)
            )
        )
    }

    @Test
    fun testGoogleRegister() {
        // click
        composeTestRule
            .onNodeWithTag("btn_register_google")
            .performClick()
//        Intents.intended(
//            allOf(
//                IntentMatchers.hasComponent(LoginActivity::class.java.name),
//                IntentMatchers.hasExtra("from","RegisterWithGoogle")
//            )
//        )
    }

    @Test
    fun testBtnRegisterInvalidEmail() {
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
            .onNodeWithTag("btn_register")
            .performClick()

        onView(withText(startsWith("email is badly formatted")))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }

    @Test
    fun testBtnRegisterInvalidEmailAndPassword() {
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
            .onNodeWithTag("btn_register")
            .performClick()

        onView(withText(startsWith("email is badly formatted")))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }

    @Test
    fun testBtnRegisterInvalidPassword() {
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
            .onNodeWithTag("btn_register")
            .performClick()

        onView(withText(startsWith("The given password is invalid.")))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }
    @Test
    fun testBtnRegisterEmptyNameAndSurname() {
        // click
        composeTestRule
            .onNodeWithTag("text_name")
            .performClick()
            .performTextInput(name)
        composeTestRule
            .onNodeWithTag("text_surname")
            .performClick()
            .performTextInput(surname)
        composeTestRule
            .onNodeWithTag("btn_register")
            .performClick()
        onView(withText(R.string.error_empty_register))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }

    @Test
    fun testBtnRegisterEmptyValues() {
        // click
        composeTestRule
            .onNodeWithTag("btn_register")
            .performClick()
        onView(withText(R.string.error_empty_register))
            .inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
    }

    @Test
    fun testBtnRegisterAlreadyRegistered() {
        // click
        composeTestRule
            .onNodeWithTag("text_name")
            .performClick()
            .performTextInput(name)
        composeTestRule
            .onNodeWithTag("text_surname")
            .performClick()
            .performTextInput(surname)
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
            .onNodeWithTag("btn_register")
            .performClick()

        // TODO - mock auth to test successfully register
//        onView(withText(com.github.mateo762.myapplication.R.string.success_register))
//            .inRoot(ToastMatcher().apply {
//                matches(isDisplayed())
//            })
//        Intents.intended(
//            allOf(
//                IntentMatchers.hasComponent(BaseActivity::class.java.name)
//            )
//        )
    }

     */
}