package com.github.mateo762.myapplication.profile

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.profile.ProfileActivity.Companion.USER_ID_EXTRA
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@ExperimentalCoroutinesApi
class ProfileActivityTestWithIntent {

    val intent = Intent(ApplicationProvider.getApplicationContext(), ProfileActivity.EntryPoint::class.java).apply {
        putExtra(USER_ID_EXTRA, "user2")
    }

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule<ProfileActivity.EntryPoint>(intent)

    @Before
    fun setUp() {
    }

    @Test
    fun testFollowButton() {
        onView(ViewMatchers.withId(R.id.btnEdit)).check(matches(not(isDisplayed())))
        onView(ViewMatchers.withId(R.id.btnSave)).check(matches(not(isDisplayed())))

        onView(ViewMatchers.withId(R.id.btnFollow)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.btnUnfollow)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testOnFollowButtonClicked() = runTest {
        onView(ViewMatchers.withId(R.id.btnFollow)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.btnFollow)).perform(click())

        onView(ViewMatchers.withId(R.id.btnUnfollow)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.btnFollow)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testOnUnFollowButtonClicked() = runTest {
        onView(ViewMatchers.withId(R.id.btnFollow)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.btnFollow)).perform(click())

        onView(ViewMatchers.withId(R.id.btnUnfollow)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.btnFollow)).check(matches(not(isDisplayed())))

        onView(ViewMatchers.withId(R.id.btnUnfollow)).perform(click())
        onView(ViewMatchers.withId(R.id.btnFollow)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.btnUnfollow)).check(matches(not(isDisplayed())))
    }
}