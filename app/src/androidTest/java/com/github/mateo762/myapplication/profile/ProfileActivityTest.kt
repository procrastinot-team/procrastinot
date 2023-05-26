package com.github.mateo762.myapplication.profile

import android.content.Context
import android.widget.ProgressBar
import android.widget.RatingBar
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.atPosition
import com.github.mateo762.myapplication.username.UsernameActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ProfileActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileActivity.EntryPoint::class.java)

    private lateinit var context: Context

    private lateinit var fiveStarProgress: ProgressBar
    private lateinit var fourStarProgress: ProgressBar
    private lateinit var threeStarProgress: ProgressBar
    private lateinit var twoStarProgress: ProgressBar
    private lateinit var oneStarProgress: ProgressBar
    private lateinit var ratingBar: RatingBar

    private lateinit var uiDevice: UiDevice

    @Before
    fun setUp() {
        hiltRule.inject()
        Intents.init()

        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        activityRule.scenario.onActivity {
            it.runOnUiThread {
                fiveStarProgress = it.findViewById(R.id.fiveStarsProgress)
                fourStarProgress = it.findViewById(R.id.fourStarsProgress)
                threeStarProgress = it.findViewById(R.id.threeStarsProgress)
                twoStarProgress = it.findViewById(R.id.twoStarsProgress)
                oneStarProgress = it.findViewById(R.id.oneStarsProgress)
                ratingBar = it.findViewById(R.id.ratingBar)
            }
        }

        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testProfileActivity_isDisplayed() {
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()))
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
    fun testInitialUserDataComponents() {
        onView(withId(R.id.nameTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.nameEditText)).check(matches(not(isEnabled())))
        onView(withId(R.id.nameEditText)).check(matches(not(isClickable())))
        onView(withId(R.id.nameEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emailTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText)).check(matches(not(isEnabled())))
        onView(withId(R.id.emailEditText)).check(matches(not(isClickable())))
        onView(withId(R.id.emailEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.usernameTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.changeUsernameButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSave)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btnFollow)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnUnfollow)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testOnEditButtonClicked() {
        onView(withId(R.id.btnEdit)).perform(click())

        onView(withId(R.id.nameTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.nameEditText)).check(matches(isEnabled()))
        onView(withId(R.id.nameEditText)).check(matches(isClickable()))
        onView(withId(R.id.nameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.emailTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emailEditText)).check(matches(isEnabled()))
        onView(withId(R.id.emailEditText)).check(matches(isClickable()))
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.usernameTextView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.changeUsernameButton)).check(matches(isDisplayed()))

        onView(withId(R.id.btnEdit)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnSave)).check(matches(isDisplayed()))

        onView(withId(R.id.nameEditText)).check(matches(withText("Joe")))
        onView(withId(R.id.emailEditText)).check(matches(withText("Joe@test.com")))
    }

    @Test
    fun testOnSaveButtonClicked() {
        onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
        onView(withId(R.id.btnEdit)).perform(click())
        onView(withId(R.id.btnSave)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSave)).perform(click())

        onView(withId(R.id.nameTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.nameEditText)).check(matches(not(isEnabled())))
        onView(withId(R.id.nameEditText)).check(matches(not(isClickable())))
        onView(withId(R.id.nameEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.emailTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.emailEditText)).check(matches(not(isEnabled())))
        onView(withId(R.id.emailEditText)).check(matches(not(isClickable())))
        onView(withId(R.id.emailEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.usernameTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.changeUsernameButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.btnEdit)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSave)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testOnChangeUsernameButtonClicked() {
        onView(withId(R.id.btnEdit)).perform(click())
        onView(withId(R.id.changeUsernameButton)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(UsernameActivity.EntryPoint::class.java.name))
    }

    @Test
    fun testUserStats() = runTest {
        onView(withId(R.id.habitCountText)).check(matches(isDisplayed()))
        onView(withId(R.id.habitCountText)).check(matches(withText(context.getString(R.string.posted_habits, 2))))
        onView(withId(R.id.avgPerWeekText)).check(matches(isDisplayed()))
        onView(withId(R.id.avgPerWeekText)).check(matches(withText(context.getString(R.string.avg_days_week, 2))))
        onView(withId(R.id.earliestTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.earliestTextView)).check(matches(withText(context.getString(R.string.earlystart, "0:0"))))
        onView(withId(R.id.latestTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.latestTextView)).check(matches(withText(context.getString(R.string.lateend, "23:59"))))
    }

    @Test
    fun testFollowingStats() = runTest {
        onView(withId(R.id.followingTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.followingTextView)).check(matches(withText(context.getString(R.string.following, 3))))
        onView(withId(R.id.followersTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.followersTextView)).check(matches(withText(context.getString(R.string.followers, 2))))
    }

    @Test
    fun testHabitsRecyclerView() = runTest {
        val appViews = UiScrollable(
            UiSelector().scrollable(true)
        )

        appViews.scrollForward()
        onView(withId(R.id.habitsRecyclerView)).check(matches(atPosition(0, hasDescendant(withText("Play guitar: MONDAY, FRIDAY")))))
        onView(withId(R.id.habitsRecyclerView)).check(matches(atPosition(1, hasDescendant(withText("Sing: MONDAY, FRIDAY")))))
    }

    @Test
    fun testHabitsImageRecyclerView() = runTest {
        val appViews = UiScrollable(
            UiSelector().scrollable(true)
        )

        appViews.scrollForward()
        onView(withId(R.id.galleryRecyclerView)).check(matches(atPosition(0, isDisplayed())))
        onView(withId(R.id.galleryRecyclerView)).check(matches(atPosition(1, isDisplayed())))
    }

    @Test
    fun testRatingDisplayed() = runTest {
        onView(withId(R.id.coachRatingView)).check(matches(isDisplayed()))

        advanceUntilIdle()

        onView(withId(R.id.coachRatingTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingNumber)).check(matches(isDisplayed()))
        onView(withId(R.id.ratingNumber)).check(matches(withText("4.5")))
        onView(withId(R.id.totalRatings)).check(matches(isDisplayed()))
        onView(withId(R.id.totalRatings)).check(matches(withText(context.getString(R.string.total_number_ratings, 2))))
        onView(withId(R.id.fiveStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.fourStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.threeStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.twoStarsProgress)).check(matches(isDisplayed()))
        onView(withId(R.id.oneStarsProgress)).check(matches(isDisplayed()))

        assertEquals(fiveStarProgress.progress, 50)
        assertEquals(fourStarProgress.progress, 50)
        assertEquals(threeStarProgress.progress, 0)
        assertEquals(twoStarProgress.progress, 0)
        assertEquals(oneStarProgress.progress, 0)
        assertEquals(ratingBar.rating, 4.5f)
    }
}