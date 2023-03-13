package com.github.mateo762.myapplication.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.github.mateo762.myapplication.MainActivity
import com.github.mateo762.myapplication.R
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalendarFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var uiDevice: UiDevice
    private lateinit var context: Context
    private var timeout = 10000L
    private lateinit var decorView: View

    @Before
    fun setUp() {
        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }

        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        context = ApplicationProvider.getApplicationContext()
        timeout = 10000L

        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testNotificationShown() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.nav_calendar)).check(matches(isDisplayed()))
        onView(withId(R.id.nav_calendar)).perform(ViewActions.click())

        onView(withId(R.id.permissionButton)).perform(ViewActions.click())

        val permissionStatus =
            ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )

        if (PackageManager.PERMISSION_DENIED == permissionStatus) {
            uiDevice.wait(Until.hasObject(By.textStartsWith("Allow")), timeout)
            uiDevice.findObject(UiSelector().text(("Allow"))).click()
        }

        val expectedTitle = "Habit reminder"
        val expectedContent = "Please finish your habit"

        onView(withId(R.id.notificationButton)).perform(ViewActions.click())

        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.textStartsWith(expectedTitle)), timeout)
        val title: UiObject2 = uiDevice.findObject(By.text(expectedTitle))
        val text: UiObject2 = uiDevice.findObject(By.textStartsWith(expectedContent))
        assertEquals(expectedTitle, title.text)
        assertTrue(text.text.startsWith(expectedContent))
        uiDevice.findObject(By.textStartsWith("Clear all")).click()
    }
}