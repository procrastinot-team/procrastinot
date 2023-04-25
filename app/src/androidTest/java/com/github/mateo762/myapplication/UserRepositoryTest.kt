package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.room.UserEntity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.mockito.invocation.InvocationOnMock


class UserRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

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

    @After
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()
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

    @Test
    fun followUserTest() = testScope.runBlockingTest {
        val currentUserId = "currentUserId"
        val targetUserId = "targetUserId"

        val currentUser = UserEntity(currentUserId, "name1","currentUser", "current@example.com", emptyList(), emptyList())
        val targetUser = UserEntity(targetUserId, "name2","targetUser", "target@example.com", emptyList(), emptyList())

        `when`(userRepository.getUser(currentUserId)).thenReturn(currentUser)
        `when`(userRepository.getUser(targetUserId)).thenReturn(targetUser)

        doAnswer { invocation ->
            val dataSnapshot = mock(DataSnapshot::class.java)
            val pushReference = mock(DatabaseReference::class.java)

            `when`(dataSnapshot.ref).thenReturn(pushReference)
            invocation.mockSetData(pushReference, currentUser.followingUsers + targetUserId)
            Unit
        }.`when`(usersReference.child(currentUserId).child("followingPath")).push()

        doAnswer { invocation ->
            val dataSnapshot = mock(DataSnapshot::class.java)
            val pushReference = mock(DatabaseReference::class.java)

            `when`(dataSnapshot.ref).thenReturn(pushReference)
            invocation.mockSetData(pushReference, targetUser.followerUsers + currentUserId)
            Unit
        }.`when`(usersReference.child(targetUserId).child("followersPath")).push()

        userRepository.followUser(currentUserId, targetUserId)

        verify(usersReference.child(currentUserId).child("followingPath")).push()
        verify(usersReference.child(targetUserId).child("followersPath")).push()
    }


    private fun InvocationOnMock.mockSetData(reference: DatabaseReference, value: Any): Task<Void> {
        val taskCompletionSource = TaskCompletionSource<Void>()
        val task = taskCompletionSource.task

        `when`(reference.setValue(value)).thenReturn(task)

        taskCompletionSource.setResult(null)
        return task
    }
}