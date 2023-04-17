package com.github.mateo762.myapplication

import android.os.SystemClock
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alamkanak.weekview.WeekView
import com.github.mateo762.myapplication.habits.HabitsActivity
import com.github.mateo762.myapplication.habits.fragments.week.WeekFragment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import com.github.mateo762.myapplication.TestData.emptyHabits
import com.github.mateo762.myapplication.TestData.hardCodedHabits
import com.github.mateo762.myapplication.TestData.hardCodedImages
import com.github.mateo762.myapplication.TestData.noTodayHabits
import com.github.mateo762.myapplication.TestData.testHabits
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.util.*

/*
@RunWith(AndroidJUnit4::class)
class WeekViewFragmentTest {

    private lateinit var testFragment :WeekFragment

    @Before
    fun setUp() {
        testFragment = WeekFragment().apply { habits = testHabits }
    }
    @Test
    fun weekView_isDisplayed() {
        // Launch the WeekFragment in a container
        launchFragmentInContainer<WeekFragment>()


        // Add assertions for the expected events displayed in the WeekView
        onView(withId(R.id.weekView))
            .check(matches(isDisplayed()))

    }


    @Test
    fun weekView_displaysCorrectNumberOfVisibleDays() {
        launchFragmentInContainer<WeekFragment>()

        val expectedVisibleDays =
            7 // Replace this with the number of visible days you set in WeekFragment
        onView(
            allOf(
                withId(R.id.weekView),
                withVisibleDays(expectedVisibleDays)
            )
        ).check(matches(isDisplayed()))
    }

    private fun withVisibleDays(visibleDays: Int): Matcher<View> {
        return object : BoundedMatcher<View, WeekView>(WeekView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with visible days: ").appendValue(visibleDays)
            }

            override fun matchesSafely(item: WeekView): Boolean {
                return item.numberOfVisibleDays == visibleDays
            }
        }
    }
}*/
