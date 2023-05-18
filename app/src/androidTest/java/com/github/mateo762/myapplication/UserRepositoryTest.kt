package com.github.mateo762.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.followers.UserRepositoryImpl
import com.github.mateo762.myapplication.models.UserEntity
import com.google.firebase.database.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class UserRepositoryTest {

    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var database: DatabaseReference
    private lateinit var usersReference: DatabaseReference
    private lateinit var childReference: DatabaseReference
    private lateinit var userRepositoryImplTest: UserRepositoryImpl

    @Before
    fun setup() {
        database = mock(DatabaseReference::class.java)
        usersReference = mock(DatabaseReference::class.java)
        childReference = mock(DatabaseReference::class.java)

        `when`(database.child("users")).thenReturn(usersReference)
        `when`(usersReference.child(anyString())).thenReturn(childReference)

        userRepositoryImpl = UserRepositoryImpl(database)
    }

    @Test
    fun getUserTest() = runTest {
        val testUserId = "testUserId"
        val dataSnapshot = mock(DataSnapshot::class.java)
        val userEntity = UserEntity("uid", "testUserId", "testUsername", "testEmail", emptyList(), emptyList())

        `when`(dataSnapshot.getValue(UserEntity::class.java)).thenReturn(userEntity)

        doAnswer { invocation ->
            val listener = invocation.getArgument<ValueEventListener>(0)
            listener.onDataChange(dataSnapshot)
        }.`when`(childReference).addListenerForSingleValueEvent(any())

        val result = userRepositoryImpl.getUser(testUserId)
        verify(usersReference).child(testUserId)
        assertEquals(userEntity, result)
    }

    /*
    @Test
    fun followUserTest() = runTest {
        try {
            userRepositoryImplTest = UserRepositoryImpl(database)
            userRepositoryImplTest.followUser("uT8hhonn2lR0vnfdDS8PszDhnZJ2", "LamjUsoWfPR62uZ1nwFcFMBYW912")
        } catch (e: Exception) {
            assertTrue(false)
        }
        assertTrue(true)
    }
    @Test
    fun unfollowUserTest() {
        try {
            userRepositoryImplTest = UserRepositoryImpl(database)
            userRepositoryImplTest.unfollowUser("uT8hhonn2lR0vnfdDS8PszDhnZJ2", "LamjUsoWfPR62uZ1nwFcFMBYW912")
        } catch (e: Exception) {
            assertTrue(false)
        }
        assertTrue(true)
    }

    @Test
    fun checkIfUserFollowsTest() = runTest {
        try {
            userRepositoryImplTest = UserRepositoryImpl(database)
            val result = userRepositoryImplTest.checkIfUserFollows("uT8hhonn2lR0vnfdDS8PszDhnZJ2", "LamjUsoWfPR62uZ1nwFcFMBYW912")
            // Add an assertion to verify the result if needed, e.g.:
            // assertEquals(expectedValue, result)
        } catch (e: Exception) {
            assertTrue(false)
        }
        assertTrue(true)
    }
*/
}