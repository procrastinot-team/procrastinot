package com.github.mateo762.myapplication

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher
import org.hamcrest.Matchers
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
    private var habitsCollected: ArrayList<String> = ArrayList()
    private var habitsCoaches: ArrayList<String> = ArrayList()


    @get:Rule
    public val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA)


    @Before
    fun setUp() {
        // use get activity
        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }
        var currentUser = "testUser"
        var firebaseUser = Firebase.auth.currentUser?.uid
        if (firebaseUser != null) {
            currentUser = firebaseUser.toString()
        }
        val db = Firebase.database.reference
        val ref =  db.child("users").child(currentUser).child("habitsPath")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                Log.d("TakePictureTest", "Habits exist")
                Log.d("TakePictureTest", it.toString())
                var children = it.children
                for (child in children) {
                    // write child value to object
                    var habitName = child.child("name").value.toString()
                    var coach = child.child("coach").value.toString()

                    habitsCollected += habitName
                    habitsCoaches += coach
                }
            }
        }
        Thread.sleep(500)
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

    @Test
    fun checkIfTakePhotoButtonIsDisplayed() {
        onView(withId(R.id.takePhotoButton)).check(matches(isDisplayed()))
    }

    // If disabled it will fail to click
    @Test
    fun checkIfDropdownIsDisplayed() {
       if (habitsCollected.size > 0) {
           onView(withId(R.id.textInputLayout)).perform(ViewActions.click())
       }
    }

    @Test
    fun testDropdown() {
        if (habitsCollected.size > 0) {
            onView(withId(R.id.textInputLayout)).perform(ViewActions.click())
            Log.d("TakePictureTest", habitsCollected.toString())
            onData(Matchers.equalTo(habitsCollected[0])).inRoot(isPlatformPopup()).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.auto_complete_txt)).check(ViewAssertions.matches(withText(habitsCollected[0])))
        }
    }

    @Test
    fun selectAndClick() {
        if (habitsCollected.size > 0) {
            onView(withId(R.id.textInputLayout)).perform(ViewActions.click())
            Log.d("TakePictureTest", habitsCollected.toString())
            onData(Matchers.equalTo(habitsCollected[0])).inRoot(isPlatformPopup()).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.auto_complete_txt)).check(ViewAssertions.matches(withText(habitsCollected[0])))
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            Thread.sleep(2000)
            // go back
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            uiDevice.pressBack()
        }
    }

    @Test
    fun selectAndClickAndTakePicture() {
        if (habitsCollected.size > 0) {
            onView(withId(R.id.textInputLayout)).perform(ViewActions.click())
            Log.d("TakePictureTest", habitsCollected.toString())
            onData(Matchers.equalTo(habitsCollected[0])).inRoot(isPlatformPopup()).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.auto_complete_txt)).check(ViewAssertions.matches(withText(habitsCollected[0])))
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            Thread.sleep(2000)
            // go back
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            // If the device has a physical shutter button, use it
            if (uiShutter.exists()) {
                uiShutter.click()
            }
            // Go back to the app
            uiDevice.pressBack()
        }
    }

    @Test
    fun takePhotoAndShutterAndDone() {
        if (habitsCollected.size > 0) {
            onView(withId(R.id.textInputLayout)).perform(ViewActions.click())
            Log.d("TakePictureTest", habitsCollected.toString())
            onData(Matchers.equalTo(habitsCollected[0])).inRoot(isPlatformPopup()).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.auto_complete_txt)).check(ViewAssertions.matches(withText(habitsCollected[0])))
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            Thread.sleep(2000)
            // go back
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            // If the device has a physical shutter button, use it
            if (uiShutter.exists()) {
                uiShutter.click()
            }
            // accept the image
            var uiAccept = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
            if (uiAccept.exists()) {
                uiAccept.click()
            }
        }
    }

    @Test
    fun takePhotoAndShutterAndDoneAndSave() {
        Log.d("TakePictureTest", habitsCoaches.toString())
        if (habitsCollected.size > 0) {
            onView(withId(R.id.textInputLayout)).perform(ViewActions.click())
            Log.d("TakePictureTest", habitsCollected.toString())
            onData(Matchers.equalTo(habitsCollected[0])).inRoot(isPlatformPopup()).perform(ViewActions.click())
            onView(ViewMatchers.withId(R.id.auto_complete_txt)).check(ViewAssertions.matches(withText(habitsCollected[0])))
            onView(withId(R.id.takePhotoButton)).perform(ViewActions.click())
            Thread.sleep(2000)
            // go back
            var uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            var uiShutter = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/shutter_button"))
            // If the device has a physical shutter button, use it
            if (uiShutter.exists()) {
                uiShutter.click()
            }
            // accept the image
            var uiAccept = uiDevice.findObject(UiSelector().resourceId("com.android.camera2:id/done_button"))
            if (uiAccept.exists()) {
                uiAccept.click()
            }
            // Wait for the image to be saved takes a while
            if (habitsCoaches[0] == "null") {
                Thread.sleep(3000)
                onView(withId(R.id.backHomeButton)).perform(ViewActions.click())
            }
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
