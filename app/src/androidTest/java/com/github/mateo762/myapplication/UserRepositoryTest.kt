package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.room.UserEntity
import com.google.firebase.database.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


class UserRepositoryTest {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)


    private lateinit var userRepository: UserRepository
    private lateinit var database: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var childReference: DatabaseReference

    @Before
    fun setup() {
        database = mock(FirebaseDatabase::class.java)
        usersReference = mock(DatabaseReference::class.java)
        childReference = mock(DatabaseReference::class.java)

        `when`(database.getReference("users")).thenReturn(usersReference)
        `when`(usersReference.child(anyString())).thenReturn(childReference)

        userRepository = UserRepository(database)
    }

    @Test
    fun getUserTest() = runBlockingTest {
        val testUserId = "testUserId"
        val dataSnapshot = mock(DataSnapshot::class.java)
        val userEntity = UserEntity("uid", "testUserId", "testUsername", "testEmail", emptyList(), emptyList())

        `when`(dataSnapshot.getValue(UserEntity::class.java)).thenReturn(userEntity)

        doAnswer { invocation ->
            val listener = invocation.getArgument<ValueEventListener>(0)
            listener.onDataChange(dataSnapshot)
            Unit
        }.`when`(childReference).addListenerForSingleValueEvent(any())

        val result = userRepository.getUser(testUserId)
        verify(usersReference).child(testUserId)
        assertEquals(userEntity, result)
    }

}