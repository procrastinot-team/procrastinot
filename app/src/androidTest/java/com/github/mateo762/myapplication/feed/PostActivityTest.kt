package com.github.mateo762.myapplication.feed

import android.content.Intent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.ToastMatcher
import com.github.mateo762.myapplication.post.PostActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostActivityTest {

    private val caption = "TEST_CAPTION"
    private val body = "TEST_BODY"
    private val username = "TEST_USERNAME"

    // See ComposeTestExtensionHelper, it includes an auxiliary method to enable
    // custom intent creation prior to test setup to include the post elements to check
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun testPostContentsDisplayed() = composeRule.launch<PostActivity>(
        intentFactory = {
            Intent(it, PostActivity::class.java).apply {
                putExtra("postTitle", caption)
                putExtra("postBody", body)
                putExtra("postUsername", username)
                // Note: Image contents is untested since atm it uses a local test image
            }
        },
        onAfterLaunched = {
            // Assertions on the view
            onNodeWithTag("post_title").assertIsDisplayed()
            onNodeWithTag("post_body").assertIsDisplayed()
            onNodeWithTag("post_image").assertIsDisplayed()
            // Additional option to find elements from sub-element UserCard
            // that contains the avatar and username
            onNodeWithTag("post_user_card", useUnmergedTree = true).assertIsDisplayed()
            onNodeWithTag("user_card_avatar", useUnmergedTree = true).assertIsDisplayed()
            onNodeWithTag("user_card_username", useUnmergedTree = true).assertIsDisplayed()
        })

    @Test
    fun testUserCardOnClick() = composeRule.launch<PostActivity>(
        intentFactory = {
            Intent(it, PostActivity::class.java).apply {
                putExtra("postTitle", caption)
                putExtra("postBody", body)
                putExtra("postUsername", username)
                // Note: Image contents is untested since atm it uses a local test image
            }
        },
        onAfterLaunched = {
            // Assertions on the view
            onNodeWithTag("post_user_card").performScrollTo().performClick()
            // Check the temporary Toast appears indicating traversal to user's profile View
            Espresso.onView(ViewMatchers.withText(Matchers.startsWith("This takes you to")))
                .inRoot(ToastMatcher().apply {
                    matches(ViewMatchers.isDisplayed())
                })

        })
}