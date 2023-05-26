package com.github.mateo762.myapplication.username

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.ArraySet
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UsernameServiceFirebaseImplTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var db: DatabaseReference

    private var userId: String = UUID.randomUUID().toString()
    private var username = "username"

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun getUsernames() = runTest {
        //given
        db.child(UsernameServiceFirebaseImpl.USERNAMES_REF).child(username).setValue(userId).await()
        val service = UsernameServiceFirebaseImpl(db)
        var result: ArraySet<String>? = null

        //when
        service.getUsernames().collect {
            result = it
        }

        //then
        assertNotNull(result)
        assertNotEquals(0, result?.size)
        assertEquals(username, result?.elementAt(0))
    }

    @Test
    fun postUsernameToUsernames() {
        //given
        val service = UsernameServiceFirebaseImpl(db)

        //when
        assertDoesNotThrow { service.postUsernameToUsernames("qwer", userId) }
    }

    @Test
    fun postUsernameToUser() {
        //given
        val service = UsernameServiceFirebaseImpl(db)

        //when
        assertDoesNotThrow { service.postUsernameToUser("qwer", userId) }
    }

    @Test
    fun deleteUsername() = runTest {
        //given
        db.child(UsernameServiceFirebaseImpl.USERNAMES_REF).child(username).setValue(userId).await()
        val service = UsernameServiceFirebaseImpl(db)

        //when
        assertDoesNotThrow { service.deleteUsername(username) }
    }
}