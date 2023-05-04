package com.github.mateo762.myapplication.feed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.ui.home.FeedScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeedScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val samplePosts = listOf(
        PostEntity(
            0,
            username = "user1",
            caption = "Caption 1",
            description = "Description 1",
            datePosted = "date1",
            habitImageEntityId = "image1",
            assocHabit = "habit1",
            imageUrl = "https://example.com/image1.png"
        ),
        PostEntity(
            0,
            username = "user2",
            caption = "Caption 2",
            description = "Description 2",
            datePosted = "date2",
            habitImageEntityId = "image2",
            assocHabit = "habit2",
            imageUrl = "https://example.com/image2.png"
        ),
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