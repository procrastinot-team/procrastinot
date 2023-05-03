package com.github.mateo762.myapplication.upload

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.ui.upload.UploadImageScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UploadImageFragmentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun uploadImageScreen_displaysExpectedTexts() {
        composeTestRule.setContent {
            UploadImageScreen(
                userId = "test_user_id",
                habitId = "test_habit_id",
                image = 1
            )
        }

        composeTestRule.onNodeWithText("Upload Image").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upload Picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upload Hardcoded habits").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upload Habits").assertIsDisplayed()
    }
}
