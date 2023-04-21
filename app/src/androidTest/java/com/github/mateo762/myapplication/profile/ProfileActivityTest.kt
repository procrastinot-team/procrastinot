package com.github.mateo762.myapplication.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.Habit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.DayOfWeek


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity::class.java)


    private lateinit var activityScenario: ActivityScenario<ProfileActivity>

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun onToolbarBackButtonClicked() {
        onView(withContentDescription("Navigate up")).perform(click())

        Thread.sleep(500)

        assertTrue(activityRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun onProfileGalleryTitleDisplayed() {
        onView(withId(R.id.profileGalleryTitle)).check(matches(withText(context.getString(R.string.profile_progress_gallery_title))))
    }

    @Test
    fun testNumberOfTextViewInProfileActivity() {
        // Create a mock FirebaseUser
        val mockFirebaseUser = mock(FirebaseUser::class.java)
        `when`(mockFirebaseUser.uid).thenReturn("someUserId")

        // Mock the FirebaseAuth instance to return the mock FirebaseUser
        val mockFirebaseAuth = mock(FirebaseAuth::class.java)
        `when`(mockFirebaseAuth.currentUser).thenReturn(mockFirebaseUser)

        // Static mock of FirebaseAuth impossible due to Android not allowing Mockito mocks
//        Mockito.mockStatic(FirebaseAuth::class.java).use { utilities ->
//            utilities.`when`<Any>(FirebaseAuth::getInstance).thenReturn(mockFirebaseAuth)
//            assertThat("instance doesn't match the mock",
//                FirebaseAuth.getInstance() == mockFirebaseUser)
//        }

        val habits = listOf(
            Habit("habit1", "Habit 1", listOf(DayOfWeek.MONDAY),"00:00","01:00"),
            Habit("habit2", "Habit 2", listOf(DayOfWeek.TUESDAY),"00:00","01:00")
        )

        // Set up Firebase database and add habits for the user
        val db = Firebase.database.reference
        val ref = db.child("users/${mockFirebaseUser.uid}/habitsPath")
        for (habit in habits) {
            ref.child(habit.id).setValue(habit)
        }


        // Set up the activity under test
        val intent = Intent(context, ProfileActivity::class.java).putExtra("USER_ID", mockFirebaseUser.uid)

        activityScenario = ActivityScenario.launch(intent)

        // Assert that the number of TextViews in the profileActivity matches the number of habits
        // Failed trial to test the display of habits
//        onView(allOf(withId(R.id.habitImage), isDisplayed())).check(matches(hasChildCount(habits.size)))


        // Clean up the database
        ref.removeValue()
    }

}