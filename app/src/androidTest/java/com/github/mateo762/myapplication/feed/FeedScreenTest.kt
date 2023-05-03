package com.github.mateo762.myapplication.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.Post
import com.github.mateo762.myapplication.room.HabitImageEntity
import com.github.mateo762.myapplication.ui.home.FeedScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val samplePosts = listOf(
        Post(
            username = "user1",
            caption = "Caption 1",
            description = "Description 1",
            habitImage = HabitImageEntity("image1", "user1",
                "habit1", "https://example.com/image1.png"),
            assocHabit = "assocHabit1"
        ),
        Post(
            username = "user2",
            caption = "Caption 2",
            description = "Description 2",
            habitImage = HabitImageEntity("image2", "user2",
                "habit2", "https://example.com/image2.png"),
            assocHabit = "assocHabit2"
        )
    )

    @Test
    fun postThumbnail_isDisplayed() {
        composeTestRule.setContent {
            FeedScreen(posts = samplePosts)
        }

        composeTestRule.onAllNodesWithTag("post_thumbnail").assertCountEquals(2)
    }

    @Test
    fun postDetails_areDisplayed() {
        composeTestRule.setContent {
            FeedScreen(posts = samplePosts)
        }

        composeTestRule.onNodeWithText("Caption 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("@user1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Caption 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("@user2").assertIsDisplayed()
    }

    @Test
    fun postImages_areDisplayed() {
        composeTestRule.setContent {
            FeedScreen(posts = samplePosts)
        }

        composeTestRule.onAllNodesWithContentDescription("image_https://example.com/image1.png").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("image_https://example.com/image2.png").assertCountEquals(1)
    }
    @Test
    fun userAvatarImages_areDisplayed() {
        composeTestRule.setContent {
            FeedScreen(posts = samplePosts)
        }

        composeTestRule.onAllNodesWithContentDescription("avatar_user1").assertCountEquals(1)
        composeTestRule.onAllNodesWithContentDescription("avatar_user2").assertCountEquals(1)
    }
}