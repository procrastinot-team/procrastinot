package com.github.mateo762.myapplication.week

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alamkanak.weekview.WeekView
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.TestData.emptyHabits
import com.github.mateo762.myapplication.TestData.testHabits
import com.github.mateo762.myapplication.habits.fragments.week.WeekFragment
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4::class)
class WeekViewFragmentTest {

    private lateinit var testFragment: WeekFragment

    @Before
    fun setUp() {
        testFragment = WeekFragment()
        testFragment.isTest = true
    }

    @Test
    fun weekView_isDisplayed(){
        launchFragmentInContainer { testFragment }
        onView(withId(R.id.weekView)).check(matches(isDisplayed()))
    }

    @Test
    fun weekView_isDisplayed_withCorrectNumberOfVisibleDays() {
        launchFragmentInContainer { testFragment }

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
}
