package com.github.mateo762.myapplication.username

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mateo762.myapplication.util.State
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UsernameViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var service: UsernameService

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var viewModel: UsernameViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = UsernameViewModel(service, auth)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun isUsernameAvailable_UsernameTaken() = runTest {
        //when
        viewModel.isUsernameAvailable("walker")

        advanceUntilIdle()

        //then
        assertEquals(State.success(true), viewModel.isUsernameTaken.value)
    }

    @Test
    fun isUsernameAvailable_UsernameAvailable() = runTest {
        //when
        viewModel.isUsernameAvailable("walker123")

        advanceUntilIdle()

        //then
        assertEquals(State.success(false), viewModel.isUsernameTaken.value)
    }

    @Test
    fun pickUsername_Success() = runTest {
        //when
        viewModel.pickUsername("walker123")

        advanceUntilIdle()

        //then
        assertEquals(State.success(Unit), viewModel.postUsernameLiveData.value)
    }

    @Test
    fun isUsernameAvailable_Failure() = runTest {
        //given
        val service = MockUsernameModule.MockUsernameServiceWithException()
        viewModel = UsernameViewModel(service, auth)

        //when
        viewModel.isUsernameAvailable("walker")

        advanceUntilIdle()

        //then
        assertEquals(State.failed<Boolean>(), viewModel.isUsernameTaken.value)
    }

    @Test
    fun pickUsername_Failure() = runTest {
        //given
        val service = MockUsernameModule.MockUsernameServiceWithException()
        viewModel = UsernameViewModel(service, auth)

        //when
        viewModel.pickUsername("walker123")

        advanceUntilIdle()

        //then
        assertEquals(State.failed<Unit>(), viewModel.postUsernameLiveData.value)
    }
}