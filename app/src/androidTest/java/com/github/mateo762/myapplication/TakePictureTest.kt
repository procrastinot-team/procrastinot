package com.github.mateo762.myapplication

import android.util.Log
import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TakePictureTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TakePhotoActivity::class.java)


    @get:Rule
    var hiltRule = HiltAndroidRule(this)



    private lateinit var decorView: View


    @get:Rule
    public val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)



    @Before
    fun setUp() {
        // use get activity
        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }


        hiltRule.inject()
        Thread.sleep(500)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testButton() {
        // Perform different actions on the view based on what is the text of the button
        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
        }
        else {
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }

    @Test
    fun testDropdown() {
        Thread.sleep(500)
        // Perform different actions on the view based on what is the text of the button
        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            onView(withId(R.id.spinner)).perform(ViewActions.click())

        }
        else {
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }

    @Test
    fun testDropdownSelect() {
        Thread.sleep(500)
        // Perform different actions on the view based on what is the text of the button
        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            onView(withId(R.id.spinner)).perform(ViewActions.click())
        }
        else {
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }

    @Test
    fun testDropdownSelectImageClick() {
        Thread.sleep(500)
        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            onView(withId(R.id.spinner)).perform(ViewActions.click())
            onData(allOf(`is`(instanceOf(String::class.java)))).atPosition(0).perform(ViewActions.click())
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            Thread.sleep(500)

            // If the device has a physical shutter button, use it
            if (uiShutter.exists()) {
                uiShutter.click()
            }
            Thread.sleep(500)

            // accept the image
            var uiAccept = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
            if (uiAccept.exists()) {
                uiAccept.click()
            }
        }
        else {
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }

    @Test
    fun testDropdownSelectToScreenThree() {
        Thread.sleep(500)
        Log.d("Test", "testDropdownSelectToScreenThree")
        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            Log.d("Test", "Passed if statement")
            onView(withId(R.id.spinner)).perform(ViewActions.click())
            onData(allOf(`is`(instanceOf(String::class.java)))).atPosition(0).perform(ViewActions.click())
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            Thread.sleep(500)
            // If the device has a physical shutter button, use it
            if (uiShutter.exists()) {
                uiShutter.click()
            }
            Thread.sleep(500)
            // accept the image
            var uiAccept = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
            if (uiAccept.exists()) {
                uiAccept.click()
            }
        }
        else {
            Log.d("Test", "Failed if statement")
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }

    @Test
    fun testDropdownSelectToScreenThreeDone() {
        Thread.sleep(500)
        Log.d("Test", "testDropdownSelectToScreenThree")

        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            Log.d("Test", "Passed if statement")
            onView(withId(R.id.spinner)).perform(ViewActions.click())
            // click on the first item in the spinner
            onData(allOf(`is`(instanceOf(String::class.java)))).atPosition(0).perform(ViewActions.click())
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            // If the device has a physical shutter button, use it
            Thread.sleep(500)

            if (uiShutter.exists()) {
                uiShutter.click()
            }
            Thread.sleep(500)

            // accept the image
            var uiAccept = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
            if (uiAccept.exists()) {
                uiAccept.click()
            }
            Thread.sleep(500)
            onView(withId(R.id.textView)).check(matches(withText(containsString("Uploading"))))
        }
        else {
            Log.d("Test", "Failed if statement")
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }

    @Test
    fun testDropdownSelectToScreenThreeDoneCoach() {
        Thread.sleep(500)
        Log.d("Test", "testDropdownSelectToScreenThree")

        if (decorView.findViewById<View>(R.id.takePhotoButton).isEnabled) {
            Log.d("Test", "Passed if statement")
            onView(withId(R.id.spinner)).perform(ViewActions.click())
            // contains substring "Coach" to select the coach habit

            onData(allOf(
                `is`(instanceOf(String::class.java)), containsString("david"))).perform(ViewActions.click())
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            // If the device has a physical shutter button, use it
            Thread.sleep(500)

            if (uiShutter.exists()) {
                uiShutter.click()
            }
            Thread.sleep(500)

            // accept the image
            var uiAccept = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
            if (uiAccept.exists()) {
                uiAccept.click()
            }
            Thread.sleep(500)
            onView(withId(R.id.ratingBar)).check(matches(isDisplayed()))
            onView(withId(R.id.ratingBar)).perform(ViewActions.click())
            onView(withId(R.id.backHomeButton)).perform(ViewActions.click())
        }
        else {
            Log.d("Test", "Failed if statement")
            onView(withId(R.id.takePhotoButton)).check(matches(withText("No habits found")))
        }
    }


}
