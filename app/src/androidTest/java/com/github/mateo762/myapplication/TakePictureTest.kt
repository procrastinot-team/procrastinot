package com.github.mateo762.myapplication

import android.graphics.Point
import android.os.RemoteException
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.github.mateo762.myapplication.takephoto.TakePhotoActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TakePictureTest {

    @get:Rule
    public val activityRule = ActivityScenarioRule(TakePhotoActivity::class.java)

    //@get:Rule
    //public val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)


    @Before
    fun setUp() {
        // make the phone not go to sleep or lock
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val coordinates: Array<Point?> = arrayOfNulls<Point>(4)
        coordinates[0] = Point(248, 1520)
        coordinates[1] = Point(248, 929)
        coordinates[2] = Point(796, 1520)
        coordinates[3] = Point(796, 929)
        try {
            if (!uiDevice.isScreenOn()) {
                uiDevice.wakeUp()
                uiDevice.swipe(coordinates, 10)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
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


@Test
    fun clickTheButton() {
        onView(isRoot()).perform(waitFor(5000))
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
        onView(isRoot()).perform(waitFor(5000))
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
        onView(isRoot()).perform(waitFor(5000))
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
    }




}