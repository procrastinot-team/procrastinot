package com.github.mateo762.myapplication.profile

import android.net.Uri
import com.github.mateo762.myapplication.di.ProfileModule
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.followers.UserRepositoryImpl
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.models.UserEntity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.DayOfWeek
import java.util.UUID
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProfileModule::class]
)
class MockProfileModule {

    @Singleton
    @Provides
    fun bindProfileService(): ProfileService {
        return MockProfileService()
    }

    @Singleton
    @Provides
    fun bindUserRepository(): UserRepository {
        return MockUserRepository()
    }

    @Singleton
    @Provides
    fun bindUserImageStorageService(): UserImageStorageService {
        return MockUserImageStorageService()
    }

    class MockProfileService(): ProfileService {
        override fun getHabitsImages(userId: String): Flow<ArrayList<HabitImageEntity>> {
            return flow {
                val array = ArrayList<HabitImageEntity>()
                val habitImageEntity1 = HabitImageEntity(url = "https://images.stockfreeimages.com/402/sfixl/4025248.jpg")
                val habitImageEntity2 = HabitImageEntity(url = "https://thumbs.dreamstime.com/z/salad-strawberries-mozzarella-balls-arugula-pecans-gray-background-top-view-high-quality-photo-salad-strawberries-277935431.jpg")

                array.add(habitImageEntity1)
                array.add(habitImageEntity2)

                emit(array)
            }
        }

        override fun getHabits(userId: String): Flow<ArrayList<HabitEntity>> {
            return flow {
                val array = ArrayList<HabitEntity>()
                val habit1 = HabitEntity(
                    name = "Play guitar",
                    days = listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                    startTime = "00:01",
                    endTime = "23:58",
                )
                val habit2 = HabitEntity(
                    name = "Sing",
                    days = listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                    startTime = "00:00",
                    endTime = "23:59",
                )

                array.add(habit1)
                array.add(habit2)

                emit(array)
            }
        }
    }

    class MockUserRepository(): UserRepository {
        override fun getUserUid(): String {
            return "IWJEASmqowjoIAJi"
        }

        override suspend fun getUser(uid: String): UserEntity {
            return UserEntity(
                name = "Joe",
                email = "Joe@test.com",
                username = "johndoe",
                url = "https://images.pexels.com/photos/10761809/pexels-photo-10761809.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
            )
        }

        override suspend fun followUser(currentUserId: String, targetUserId: String) {

        }

        override fun unfollowUser(currentUserId: String, targetUserId: String) {

        }

        override suspend fun checkIfUserFollows(
            currentUserId: String,
            targetUserId: String
        ): Boolean {
            return false
        }

        override suspend fun getFollowers(currentUserId: String): List<String> {
            val array = ArrayList<String>()
            array.add("kjeriuhweAHD")
            array.add("IOAWjdkjasdkjawoiejqwe")

            return array
        }

        override suspend fun getFollowing(currentUserId: String): List<String> {
            val array = ArrayList<String>()
            array.add("kjeriuhweAHD")
            array.add("IOAWjdkjasdkjawoiejqwe")
            array.add("IOAWjdkjasdkjKJADSSiejqwe")

            return array
        }
    }

    class MockUserImageStorageService(): UserImageStorageService {
        override fun storeImage(uid: String?, imageUri: Uri?) {
            //no-op
        }

    }
}