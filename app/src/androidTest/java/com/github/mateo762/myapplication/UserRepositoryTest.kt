package com.github.mateo762.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.followers.UserRepositoryImpl
import com.github.mateo762.myapplication.models.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var db: DatabaseReference

    @Inject
    lateinit var auth: FirebaseAuth

    private var userId1: String = UUID.randomUUID().toString()
    private var userId2: String = UUID.randomUUID().toString()
    private lateinit var userRepository: UserRepository
    private lateinit var user1: UserEntity
    private lateinit var user2: UserEntity

    @Before
    fun setup() = runTest {
        hiltRule.inject()
        user1 = UserEntity(
            uid = userId1,
            name = "Joe",
            email = "Joe@test.com",
            username = "johndoe",
            url = "url"
        )
        user2 = UserEntity(
            uid = userId1,
            name = "Doe",
            email = "Doe@test.com",
            username = "doedoe",
            url = "url"
        )

        db.child("users/${userId1}").setValue(user1).await()
        db.child("users/${userId2}").setValue(user2).await()
    }

    @Test
    fun getUserTest() = runTest {
        //given
        userRepository = UserRepositoryImpl(db, auth)

        //when
        val result = userRepository.getUser(userId1)

        //then
        assertEquals(user1, result)
    }

    @Test
    fun followUser() = runTest {
        //given
        userRepository = UserRepositoryImpl(db, auth)

        //when
        assertDoesNotThrow { userRepository.followUser(userId1, userId2) }
    }

    @Test
    fun unFollowUser() = runTest {
        //given
        userRepository = UserRepositoryImpl(db, auth)

        //when
        assertDoesNotThrow { userRepository.unfollowUser(userId1, userId2) }
    }

    @Test
    fun checkIfUserFollows() = runTest {
        //given
        userRepository = UserRepositoryImpl(db, auth)
        userRepository.followUser(userId1, userId2)

        //when
        val result = userRepository.checkIfUserFollows(userId1, userId2)

        //then
        assertEquals(result, true)
    }

    @Test
    fun getFollowers() = runTest {
        //given
        userRepository = UserRepositoryImpl(db, auth)
        userRepository.followUser(userId1, userId2)

        //when
        val result = userRepository.getFollowers(userId2)

        //then
        assertEquals(result.size, 1)
        assertEquals(result.get(0), userId1)
    }

    @Test
    fun getFollowing() = runTest {
        //given
        userRepository = UserRepositoryImpl(db, auth)
        userRepository.followUser(userId1, userId2)

        //when
        val result = userRepository.getFollowing(userId1)

        //then
        assertEquals(result.size, 1)
        assertEquals(result.get(0), userId2)
    }



}