package com.github.mateo762.myapplication.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mateo762.myapplication.followers.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain

import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ProfileViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var profileService: ProfileService
    private lateinit var userRepository: UserRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var uiModel: ProfileUserInfoUiModel


    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(StandardTestDispatcher())

        auth = mock(FirebaseAuth::class.java)
        profileService = MockProfileModule.MockProfileService()
        userRepository = MockProfileModule.MockUserRepository()
        viewModel = ProfileViewModel(profileService, userRepository, auth)
        uiModel = ProfileUserInfoUiModel(
            name = "Joe",
            email = "Joe@test.com",
            username = "johndoe",
            url = "url"
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun getUserInfo_Success() = runTest {
        viewModel.getUserInfo("uid")

        advanceUntilIdle()

        assertEquals(viewModel.userInfoLiveData.value, uiModel)
    }

    @Test
    fun getHabitImages_Success() = runTest {
        viewModel.getHabitImages("uid")

        advanceUntilIdle()

        assertEquals(viewModel.habitImagesLiveData.value?.size, 2)
    }

    @Test
    fun getHabits_Success() = runTest {
        viewModel.getHabits("uid")

        advanceUntilIdle()

        assertEquals(viewModel.habitLiveData.value?.size, 2)
    }

    @Test
    fun getFollowingNumber_Success() = runTest {
        viewModel.getFollowingNumber("uid")

        advanceUntilIdle()

        assertEquals(viewModel.followingLiveData.value, 3)
    }

    @Test
    fun getFollowersNumber_Success() = runTest {
        viewModel.getFollowersNumber("uid")

        advanceUntilIdle()

        assertEquals(viewModel.followersLiveData.value, 2)
    }

    @Test
    fun getUserInfo_Failure() = runTest {
        viewModel = ProfileViewModel(
            profileService,
            MockProfileModule.MockUserRepositoryWithException(),
            auth
        )

        assertDoesNotThrow { viewModel.getUserInfo("uid") }
    }

    @Test
    fun getHabitImages_Failure() = runTest {
        viewModel = ProfileViewModel(
            profileService,
            MockProfileModule.MockUserRepositoryWithException(),
            auth
        )

        assertDoesNotThrow { viewModel.getHabitImages("uid") }
    }

    @Test
    fun getHabits_Failure() = runTest {
        viewModel = ProfileViewModel(
            profileService,
            MockProfileModule.MockUserRepositoryWithException(),
            auth
        )

        assertDoesNotThrow {
            viewModel.getHabits("uid")
        }
    }

    @Test
    fun getFollowingNumber_Failure() = runTest {
        viewModel = ProfileViewModel(
            profileService,
            MockProfileModule.MockUserRepositoryWithException(),
            auth
        )

        assertDoesNotThrow {
            viewModel.getFollowingNumber("uid")
        }
    }

    @Test
    fun getFollowersNumber_Failure() = runTest {
        viewModel.getFollowersNumber("uid")

        assertDoesNotThrow {
            viewModel.getFollowersNumber("uid")
        }
    }
}