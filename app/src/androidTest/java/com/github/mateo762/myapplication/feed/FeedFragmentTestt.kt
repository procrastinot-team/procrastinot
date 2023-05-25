package com.github.mateo762.myapplication.feed

import androidx.compose.ui.test.junit4.createComposeRule
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.models.UserEntity
import com.github.mateo762.myapplication.room.PostDao
import com.github.mateo762.myapplication.room.PostRepository
import com.github.mateo762.myapplication.ui.home.FeedScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class FeedFragmentTestt {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        composeTestRule.setContent {
            FeedScreen(posts = emptyList())
        }
    }


    @Test
    fun testFeed_fetchFollowingUsers() {
        val user1 = UserEntity("user1", "james", "james2", "james@gmail.com", emptyList(), emptyList(),
        emptyList(), emptyList()
        )
        val user2 = UserEntity("user2", "bob", "bob3", "bob@gmail.com", emptyList(), emptyList(),
        emptyList(), emptyList()
        )

        val mockDataSnapshot = mock(DataSnapshot::class.java)
        `when`(mockDataSnapshot.value).thenReturn(
            user1
        )
        val mockUserDatabaseReference = mock(DatabaseReference::class.java)
        `when`(mockDataSnapshot.ref).thenReturn(mockUserDatabaseReference)


        val feedFragment = FeedFragment()
        feedFragment.fetchFollowingUsers(
            FirebaseDatabase.getInstance().getReference("/users/mTFQAS8YmlXK89siWb36PwIe1x82"))
    }

}
