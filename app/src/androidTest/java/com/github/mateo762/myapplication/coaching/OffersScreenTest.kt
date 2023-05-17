package com.github.mateo762.myapplication.coaching

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.ui.coaching.OffersScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OffersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testRequestsPlaceholderTest() {
        setUpOffersScreen()
        composeTestRule
            .onNodeWithTag("placeholder_text")
            .assertExists()
            .assertIsDisplayed()
    }

    private fun setUpOffersScreen() {
        composeTestRule.setContent {
            OffersScreen()
        }
    }
}