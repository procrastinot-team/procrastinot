package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.PostEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PostRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var postRepository: PostRepository

    @Inject
    lateinit var database: ApplicationDatabase

    // Inject the production database dependency
    @Inject
    lateinit var productionDatabase: ApplicationDatabase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Before
    fun setupDatabase() {
        // Create a new test database instance
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testDatabase =
            Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java).build()

        // Replace the production database instance with the test database instance
        postRepository = PostRepository(testDatabase.getPostDao())
        productionDatabase.close()
        database = testDatabase
    }

    @After
    fun closeDatabase() {
        // Close the test database after the test
        database.clearAllTables()
        database.close()
    }

    @Test
    fun testPostsCache() = runBlocking {
        val posts = listOf(
            PostEntity(
                0, "test_caption_0",
                "test_desc_0",
                "test_date_0",
                "test_url_0",
                "username0",
                "assoc_habit_id_0",
                "habit_image_id_0"
            ),
            PostEntity(
                0, "test_caption_1",
                "test_desc_1",
                "test_date_1",
                "test_url_1",
                "username1",
                "assoc_habit_id_1",
                "habit_image_id_1"
            )
        )
        postRepository.insertAllPosts(posts)
        val cachedPosts = postRepository.getAllPosts()
        assertEquals(2, cachedPosts.size)
        assertEquals(posts[0], cachedPosts[0])
        assertEquals(posts[1], cachedPosts[1])
    }
}
