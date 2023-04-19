package com.github.mateo762.myapplication

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern

@RunWith(AndroidJUnit4::class)
class TakePictureTest {



    @get:Rule
    val activityRule = ActivityScenarioRule(TakePhotoActivity::class.java)

    private lateinit var decorView: View

    @Before
    fun setUp() {
        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }

        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun clickTheButton() {
        onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
    }


    @Test
    fun clickTheButtonAndTakePicture() {
        onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        Thread.sleep(2000)
        uiDevice.findObject(By.res("com.android.camera2:id/shutter_button").desc("Shutter").clazz("android.widget.ImageView").text(
            Pattern.compile("")).pkg("com.android.camera2")).clickAndWait(Until.newWindow(),
            1000)
    }

    @Test
    fun clickTheButtonAndTakePictureGoBackToApp() {
        onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        Thread.sleep(2000)
        uiDevice.findObject(By.res("com.android.camera2:id/shutter_button").desc("Shutter").clazz("android.widget.ImageView").text(
            Pattern.compile("")).pkg("com.android.camera2")).clickAndWait(Until.newWindow(),
            1000)
        Thread.sleep(2000)
        uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button")).click()
    }




}