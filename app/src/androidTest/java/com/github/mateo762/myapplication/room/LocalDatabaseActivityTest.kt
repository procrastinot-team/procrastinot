package com.github.mateo762.myapplication.room

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.PostEntity
import com.github.mateo762.myapplication.models.UserEntity
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class LocalDatabaseActivityTest {

    private val testHabitList = mutableListOf<HabitEntity>()
    private val zeroHabit = generateHabits(1).first()
    private lateinit var userDao: UserDao
    private lateinit var postDao: PostDao
    private lateinit var habitDao: HabitDao
    private lateinit var db: ApplicationDatabase


    @Before
    fun createDB() {
        testHabitList.add(zeroHabit)
        val context = ApplicationProvider.getApplicationContext<Context>()
        // db = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java).build()
        db = ApplicationDatabase.getInstance(context)
        userDao = db.getUserDao()
        postDao = db.getPostDao()
        habitDao = db.getHabitDao()
    }

    @After
    fun tearDown() {
        db.clearAllTables()
    }

    @Test
    fun testAddAndRetrieveUser() {
        val testUser: UserEntity = createTestUser("test_username")
        userDao.insert(testUser)
        // The main use of the User part of the database is to store the user's own information
        val myself = userDao.getByUsername("test_username")
        // When added to the DB, the uid is increased to preserve uniqueness,
        // so we cannot compare them by uid
        assertThat(myself.username, equalTo(testUser.username))
        assertThat(myself.habitList, equalTo(testUser.habitList))
    }

    @Test
    fun testUpdateUserInformation() {
        val testUser: UserEntity = createTestUser("test_username")
        userDao.insert(testUser)
        // Now we change the fields, using the primary key internally
        testUser.username = "updated_username"
        userDao.update(testUser)
        assertThat(testUser.username, equalTo("updated_username"))
    }


    @Test
    fun testAddAndRetrieveSingleHabit() {
        // Habit IDs are autogenerated, 0 is a placeholder value -- see HabitEntity
        val testHabit = generateHabits(1).first()
        habitDao.insertOne(testHabit)
        val retrievedHabits = habitDao.getAll()
        assertThat(retrievedHabits, containsInAnyOrder(testHabit))
    }

    @Test
    fun testAddAndRetrievePosts() {
        val testPosts = generatePosts(5)
        postDao.insertAll(*testPosts.toTypedArray()) // Process list as variable arguments
        val retrievedPosts = postDao.getAll()
        assertThat(retrievedPosts, `is`(testPosts))
    }


    @Test
    fun testDeleteHabit() {
        val testHabit = generateHabits(1).first()
        habitDao.insertOne(testHabit)
        // Delete by checking primary key of the given entity
        habitDao.delete(testHabit)
        val retrievedHabits = habitDao.getAll()
        assertThat(retrievedHabits, not(containsInAnyOrder(testHabit)))
    }

    @Test
    fun testDeletePost() {
        val testPost = generatePosts(1).first()
        postDao.insertAll(testPost)
        // Delete by checking primary key of the given entity
        postDao.delete(testPost)
        val retrievedPosts = habitDao.getAll()
        assertThat(retrievedPosts, not(containsInAnyOrder(testPost)))
    }

    @Test
    fun testAddAndRetrieveMultipleHabits() {
        // Habit IDs are autogenerated, 0 is a placeholder value -- see HabitEntity
        val testHabits = generateHabits(5)
        habitDao.insertAll(testHabits)
        val retrievedHabits = habitDao.getAll()
        assertThat(retrievedHabits, equalTo(testHabits))
    }

    private fun generateHabits(number: Int): List<HabitEntity> {
        val days = listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )
        val testHabitList = mutableListOf<HabitEntity>()
        for (i in 1..number) {
            val habitName = "habit_$i"
            val numberOfElements = Random.nextInt(6) + 1 // cannot have zero days
            val habitDays = days.asSequence().shuffled().take(numberOfElements).toList()
            val startHour = Random.nextInt(5) + 7
            val startTime = "$startHour:00"
            val endHour = Random.nextInt(5) + 13
            val endTime = "$endHour:00"
            testHabitList.add(HabitEntity(i.toString(), habitName, habitDays, startTime, endTime))
        }
        return testHabitList
    }

    private fun generatePosts(number: Int): List<PostEntity> {
        val testPostList = mutableListOf<PostEntity>()
        for (i in 1..number) {
            val postCaption = "caption of post_$i"
            val postDescription = "more detailed description of post_$i"
            val day = Random.nextInt(28) + 1
            val month = Random.nextInt(11) + 1
            val datePosted = "$day - $month - 2023"
            val contents = Random.nextBytes(5)
            testPostList.add(PostEntity(i, postCaption, postDescription, datePosted, contents))
        }
        return testPostList
    }


    private fun createTestUser(username: String): UserEntity {
        return UserEntity("1","random name", username, "username@gmail.com", testHabitList, listOf(), listOf(), listOf())
    }

}