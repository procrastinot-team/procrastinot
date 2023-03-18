package com.github.procrastinotteam.procrastinot

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import androidx.test.uiautomator.UiDevice
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsActivity::class.java)

    private lateinit var uiDevice: UiDevice
    private lateinit var context: Context
    private var timeout = 10000L
    private lateinit var decorView: View

    private val TAG = SettingsActivityTest::class.java.simpleName

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
        Thread.sleep(200)
        onView(withId(R.id.permissionButton)).perform(ViewActions.click())

        val permissionStatus =
            ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )

        Log.d(TAG, "permissionStatus = $permissionStatus")
        Log.d(TAG, "Android version = ${Build.VERSION.SDK_INT}")

        if (PackageManager.PERMISSION_DENIED == permissionStatus) {
            Log.d(TAG, "inside if")
            uiDevice.wait(Until.hasObject(By.textContains("Allow")), timeout)
            uiDevice.findObject(UiSelector().text(("Allow"))).click()
        }

        val expectedTitle = context.getString(R.string.notification_content_title)
        val expectedContent = context.getString(R.string.notification_content_text)

        onView(withId(R.id.notificationButton)).perform(ViewActions.click())

        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.textStartsWith(expectedTitle)), timeout)
        val title: UiObject2 = uiDevice.findObject(By.textStartsWith(expectedTitle))
        val text: UiObject2 = uiDevice.findObject(By.textStartsWith(expectedContent))
        assertEquals(expectedTitle, title.text)
        assertTrue(text.text.startsWith(expectedContent))
        uiDevice.findObject(By.textStartsWith("Clear all")).click()
    }
}