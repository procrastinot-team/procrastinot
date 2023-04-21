package com.github.mateo762.myapplication

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.notifications.NotificationInfoActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NotificationInfoActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(NotificationInfoActivity::class.java)


    private lateinit var uiDevice: UiDevice
    private lateinit var context: Context
    private var timeout = 10000L

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()

        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun onContentDisplayed() {
        onView(ViewMatchers.withId(R.id.title)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.description)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.topImage)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.bottomImage)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.positiveButton)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.negativeButton)).check(matches(isDisplayed()))
    }

    @Test
    fun onIAmInButtonClicked() {
        onView(ViewMatchers.withId(R.id.positiveButton)).perform(click())

        val permissionStatus =
            ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )

        if (PackageManager.PERMISSION_DENIED == permissionStatus) {
            uiDevice.wait(Until.hasObject(By.textContains("Allow")), timeout)
            uiDevice.findObject(UiSelector().text(("Allow"))).click()

            intended(hasComponent(HomeActivity::class.java.name))
        }
    }

    @Test
    fun onSkipButtonClicked() {
        onView(ViewMatchers.withId(R.id.negativeButton)).perform(click())

        intended(hasComponent(HomeActivity::class.java.name))
    }
}