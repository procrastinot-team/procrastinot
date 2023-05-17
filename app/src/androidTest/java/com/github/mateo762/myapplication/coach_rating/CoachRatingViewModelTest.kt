package com.github.mateo762.myapplication.coach_rating

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mateo762.myapplication.util.State
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class CoachRatingViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var service: RatingService

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var viewModel: CoachRatingViewModel
    private lateinit var uiModel: CoachRatingUiModel

    @Before
    fun setup() {
        hiltRule.inject()

        Dispatchers.setMain(StandardTestDispatcher())

        viewModel = CoachRatingViewModel(service, auth)
        uiModel = CoachRatingUiModel(
            totalNumberRatings = 2,
            rating = 4.5f,
            fiveStarProgress = 50,
            fourStarProgress = 50,
            threeStarProgress = 0,
            twoStarProgress = 0,
            oneStarProgress = 0
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun getRatingStats_Success() = runTest {
        //when
        viewModel.getRatingStats()

        advanceUntilIdle()

        //then
        assertEquals(State.success(uiModel), viewModel.uiModelLiveData.value)
    }

    @Test
    fun getRatingStats_EmptyRatings() = runTest {
        //given
        viewModel = CoachRatingViewModel(MockRatingModule.MockRatingServiceEmptyArray(), auth)

        //when
        viewModel.getRatingStats()

        advanceUntilIdle()

        //then
        assertEquals(State.failed<CoachRatingUiModel>("Empty ratings array"), viewModel.uiModelLiveData.value)
    }

    @Test
    fun getRatingStats_Exception() = runTest {
        //given
        viewModel = CoachRatingViewModel(MockRatingModule.MockRatingServiceWithException(), auth)

        //when
        viewModel.getRatingStats()

        advanceUntilIdle()

        //then
        assertEquals(State.failed<CoachRatingUiModel>(), viewModel.uiModelLiveData.value)
    }

}