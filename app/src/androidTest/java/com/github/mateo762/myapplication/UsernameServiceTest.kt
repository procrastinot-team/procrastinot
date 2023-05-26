package com.github.mateo762.myapplication

import androidx.collection.ArraySet
import com.github.mateo762.myapplication.username.UsernameService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UsernameServiceTest {

    private lateinit var usernameService: UsernameService
    private lateinit var usernamesFlow: MutableStateFlow<ArraySet<String>>

    @Before
    fun setUp() {
        usernamesFlow = MutableStateFlow(ArraySet())
        usernameService = object : UsernameService {
            override fun getUsernames(): Flow<ArraySet<String>> {
                return usernamesFlow
            }

            override fun postUsernameToUsernames(username: String, uid: String): Flow<Unit> {
                return flowOf(Unit).apply {
                    usernamesFlow.value.add(username)
                }
            }

            override fun postUsernameToUser(username: String, uid: String): Flow<Unit> {
                return flowOf(Unit).apply {
                    usernamesFlow.value.add(username)
                }
            }

            override fun deleteUsername(username: String) {
                usernamesFlow.value.remove(username)
            }
        }
    }

    @Test
    fun testPostUsernameToUsernames() = runTest {
        val username = "user1"
        val uid = "uid1"

        usernameService.postUsernameToUsernames(username, uid).collect {
            // No need to assert anything in this case
        }

        val usernames = usernamesFlow.value
        assertEquals(true, usernames.contains(username))
    }

    @Test
    fun testPostUsernameToUser() = runTest {
        val username = "user1"
        val uid = "uid1"

        usernameService.postUsernameToUser(username, uid).collect {
            // No need to assert anything in this case
        }

        val usernames = usernamesFlow.value
        assertEquals(true, usernames.contains(username))
    }

    @Test
    fun testDeleteUsername() {
        val username = "user1"
        val usernames = ArraySet<String>()
        usernames.add(username)
        usernamesFlow.value = usernames

        usernameService.deleteUsername(username)

        assertEquals(false, usernamesFlow.value.contains(username))
    }
}