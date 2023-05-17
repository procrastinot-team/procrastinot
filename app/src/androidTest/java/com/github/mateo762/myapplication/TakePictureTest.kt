package com.github.mateo762.myapplication

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TakePictureTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TakePhotoActivity::class.java)



    private lateinit var decorView: View


    @get:Rule
    public val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)


    @Before
    fun setUp() {
        // use get activity
        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    private fun waitFor(timeout: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $timeout milliseconds"
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(timeout)
            }
        }
    }

    fun habitsExist(): Boolean {
        var currentUser = "testUser"
        var firebaseUser = Firebase.auth.currentUser?.uid
        if (firebaseUser != null) {
            currentUser = firebaseUser.toString()
        }
        val db = Firebase.database.reference
        val ref =  db.child("users").child(currentUser).child("habitsPath")
        var final = false
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                final = true
            }
        }
        return final
    }

    @Test
    fun checkIfTakePhotoButtonIsDisplayed() {
        onView(withId(R.id.takePhotoButton)).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfDropdownIsDisplayed() {
        if (habitsExist()) {
            onView(withId(R.id.textInputLayout)).check(matches(isDisplayed()))
        }
    }


/*@Test
    fun clickTheButton() {
        onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())

    Thread.sleep(2000)
    val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    var uiPermissionButton = uiDevice.findObject(UiSelector().text("While using the app"))
    if (uiPermissionButton.exists()) {
        uiPermissionButton.click()
    }
    uiPermissionButton = uiDevice.findObject(UiSelector().text("WHILE USING THE APP"))
    if (uiPermissionButton.exists()) {
        uiPermissionButton.click()
    }
    Thread.sleep(2000)
    // Press back
    uiDevice.pressBack()
    }


    @Test
    fun clickTheButtonAndTakePicture() {
        onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())

        Thread.sleep(2000)
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var uiPermissionButton = uiDevice.findObject(UiSelector().text("While using the app"))
        if (uiPermissionButton.exists()) {
            uiPermissionButton.click()
        }
        uiPermissionButton = uiDevice.findObject(UiSelector().text("WHILE USING THE APP"))
        if (uiPermissionButton.exists()) {
            uiPermissionButton.click()
        }
        Thread.sleep(2000)
        var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
        // If the device has a physical shutter button, use it
        if (uiShutter.exists()) {
            uiShutter.click()
        }
        // Go back to the app
        uiDevice.pressBack()
    }

    @Test
    fun clickTheButtonAndTakePictureGoBackToApp() {
        onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        var uiPermissionButton = uiDevice.findObject(UiSelector().text("While using the app"))
        if (uiPermissionButton.exists()) {
            uiPermissionButton.click()
        }
        uiPermissionButton = uiDevice.findObject(UiSelector().text("WHILE USING THE APP"))
        if (uiPermissionButton.exists()) {
            uiPermissionButton.click()
        }
        var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
        // If the device has a physical shutter button, use it
        if (uiShutter.exists()) {
            uiShutter.click()
        }
        var uiDone = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
        // If the device has took a picture, select that photo. Doesnt work if there is no shutter button
        if (uiDone.exists()) {
            uiDone.click()
        }
        // Go back to the app
        uiDevice.pressBack()
    }*/




}
