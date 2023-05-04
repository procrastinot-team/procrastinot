package com.github.mateo762.myapplication.username

import androidx.collection.ArraySet
import com.github.mateo762.myapplication.di.UsernameModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UsernameModule::class]
)
class MockUsernameModule {
    private val usernameService = MockUsernameService()

    @Singleton
    @Provides
    fun provideUsernameService(): UsernameService {
        return usernameService
    }

    class MockUsernameService : UsernameService {
        private var usernames = HashMap<String, String>()
        private var users = HashMap<String, HashMap<String, String>>()

        init {
            usernames["johndoe"] = "woeirnsi40qwedfh"
            usernames["walker"] = "woeirnsi40qwedgh"
            usernames["username"] = "woeirnsi40qweder"
            usernames["tester"] = "woeirnsi40qwedqw"

            users["woeirnsi40qwedfh"] = HashMap()
            users["woeirnsi40qwedgh"] = HashMap()
            users["woeirnsi40qweder"] = HashMap()
            users["woeirnsi40qwedqw"] = HashMap()
        }

        override fun getUsernames(): Flow<ArraySet<String>> {
            return flow {
                val array = ArraySet<String>()
                for (key in usernames.keys) {
                    array.add(key)
                }
                emit(array)
            }
        }

        override fun postUsernameToUsernames(username: String, uid: String): Flow<Unit> {
            return flow {
                usernames[username] = uid
                emit(Unit)
            }
        }

        override fun postUsernameToUser(username: String, uid: String): Flow<Unit> {
            return flow {
                users[uid]?.put("username", username)
                emit(Unit)
            }
        }
    }

    class MockUsernameServiceWithException : UsernameService {
        override fun getUsernames(): Flow<ArraySet<String>> {
            throw RuntimeException()
        }

        override fun postUsernameToUsernames(username: String, uid: String): Flow<Unit> {
            throw RuntimeException()
        }

        override fun postUsernameToUser(username: String, uid: String): Flow<Unit> {
            throw RuntimeException()
        }
    }
}