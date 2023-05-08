package com.github.mateo762.myapplication.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.mateo762.myapplication.ui.habits.CreateHabitScreen
import org.junit.Rule
import org.junit.Test

class CreateHabitScreenTest {


    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun createHabitScreen_displaysCreateNewHabitText() {

        composeTestRule.setContent {
            CreateHabitScreen()
        }

        composeTestRule.onNodeWithText("Create new habit")
            .assertIsDisplayed()
    }

}