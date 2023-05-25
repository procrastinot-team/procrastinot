package com.github.mateo762.myapplication.feed

import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.home.fragments.FeedFragment
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.room.PostDao
import com.github.mateo762.myapplication.room.PostRepository
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class FeedFragmentTestt {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var feedFragment: FeedFragment

    @Before
    fun setup() {
        hiltRule.inject()
        feedFragment = FeedFragment()
        // Mock the PostRepository
        val dummyPostDao = object : PostDao {
            override fun getAll(): List<PostEntity> {
                return listOf()
            }

            override fun insertAll(posts: List<PostEntity>) {
                return
            }

            override fun delete(post: PostEntity) {
                return
            }

        }
        feedFragment.postRepository = PostRepository(dummyPostDao)
    }

/*    @Test
    fun fetchFollowingUsersTest() {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val userReference = firebaseDatabase.getReference("dummy")
        feedFragment.fetchFollowingUsers(userReference)
    }*/

 /*   @Test
    fun fetchUserImagesTest() {
        feedFragment.fetchUserImages("dummyId")
    }*/

 /*   @Test
    fun generatePostsTest() {
        feedFragment.generatePosts(listOf())
    }*/

    @Test
    fun updatePostsCacheTest() {
        feedFragment.updatePostsCache(listOf())
    }

    @Test
    fun getLocalPostsTest() {
        feedFragment.getLocalPosts()
    }
}
