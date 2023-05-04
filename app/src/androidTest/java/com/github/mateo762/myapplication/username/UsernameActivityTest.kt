package com.github.mateo762.myapplication.username

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.ToastMatcher
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.notifications.NotificationInfoActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UsernameActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(UsernameActivity::class.java)

    private lateinit var usernameEditText: EditText
    private lateinit var context: Context
    private lateinit var mActivity: Activity
    private lateinit var notificationManager: NotificationManager

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()

        context = ApplicationProvider.getApplicationContext()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                mActivity = activity
                usernameEditText = activity.findViewById(R.id.username)
            }
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testInitialState() {
        onView(withId(R.id.loadingContainer)).check(matches(not(isDisplayed())))
        onView(withId(R.id.continueButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.usernameFeedback)).check(matches(withText(R.string.choose_username_feedback_minimum_characters)))
        onView(withId(R.id.usernameFeedback)).check(matches(hasTextColor(R.color.red)))
    }

    @Test
    fun testUsernameAlreadyTaken() = runTest {
        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                usernameEditText.setText("walker")
            }
        }

        onView(withId(R.id.continueButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.usernameFeedback)).check(matches(withText(R.string.choose_username_feedback_username_taken)))
        onView(withId(R.id.usernameFeedback)).check(matches(hasTextColor(R.color.red)))
    }

    @Test
    fun testUsernameAvailable() = runTest {
        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                usernameEditText.setText("student")
            }
        }

        onView(withId(R.id.continueButton)).check(matches(isEnabled()))
        onView(withId(R.id.usernameFeedback)).check(matches(withText(R.string.choose_username_feedback_username_available)))
        onView(withId(R.id.usernameFeedback)).check(matches(hasTextColor(R.color.green)))
    }

    @Test
    fun testUsernameSwitchFromAvailableToTaken() = runTest {
        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                usernameEditText.setText("walke")
            }
        }

        onView(withId(R.id.continueButton)).check(matches(isEnabled()))
        onView(withId(R.id.usernameFeedback)).check(matches(withText(R.string.choose_username_feedback_username_available)))
        onView(withId(R.id.usernameFeedback)).check(matches(hasTextColor(R.color.green)))

        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                usernameEditText.setText("walker")
            }
        }

        onView(withId(R.id.continueButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.usernameFeedback)).check(matches(withText(R.string.choose_username_feedback_username_taken)))
        onView(withId(R.id.usernameFeedback)).check(matches(hasTextColor(R.color.red)))
    }

    @Test
    fun testChooseAvailableUsername() = runTest {
        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                usernameEditText.setText("james")
            }
        }

        onView(withId(R.id.continueButton)).check(matches(isEnabled()))
        onView(withId(R.id.usernameFeedback)).check(matches(withText(R.string.choose_username_feedback_username_available)))
        onView(withId(R.id.usernameFeedback)).check(matches(hasTextColor(R.color.green)))

        onView(withId(R.id.continueButton)).perform(click())

        onView(withText(Matchers.startsWith(context.getString(R.string.choose_username_pick_username_success)))).inRoot(
            ToastMatcher().apply {
                matches(isDisplayed())
            })
        if (mActivity.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) || !notificationManager.areNotificationsEnabled()) {
            Intents.intended(IntentMatchers.hasComponent(NotificationInfoActivity::class.java.name))
        } else {
            Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
        }
    }

    @Test
    fun testBlankUsername() = runTest {
        activityRule.scenario.onActivity { activity ->
            activity.runOnUiThread {
                usernameEditText.setText(" ")
            }
        }

        onView(withId(R.id.continueButton)).perform(click())

        onView(withText(Matchers.startsWith(context.getString(R.string.choose_username_empty_username_message)))).inRoot(
            ToastMatcher().apply {
                matches(isDisplayed())
            })
    }
}