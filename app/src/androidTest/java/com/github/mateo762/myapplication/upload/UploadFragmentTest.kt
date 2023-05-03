package com.github.mateo762.myapplication.upload

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.UploadFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UploadFragmentTest {

    private lateinit var scenario: FragmentScenario<UploadFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }

    @Test
    fun uploadFragmentIsDisplayed() {
        onView(ViewMatchers.withContentDescription("upload_compose_view")).check(matches(isDisplayed()))
    }
}