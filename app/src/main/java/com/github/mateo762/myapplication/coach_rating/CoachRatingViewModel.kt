package com.github.mateo762.myapplication.coach_rating

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mateo762.myapplication.util.State
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoachRatingViewModel @Inject constructor(
    private val service: RatingService,
    private val auth: FirebaseAuth
) : ViewModel() {

    var uiModelLiveData = MutableLiveData<State<CoachRatingUiModel>>()

    companion object {
        private val TAG = CoachRatingViewModel::class.java.simpleName
        private const val MAX_PERCENTAGE_PROGRESS = 100
    }

    private var oneStarRatings = 0
    private var twoStarRatings = 0
    private var threeStarRatings = 0
    private var fourStarRatings = 0
    private var fiveStarRatings = 0
    private var totalNumberRatings = 0

    /**
     * Method that gets the rating stats.
     */
    fun getRatingStats(uid: String) {
        resetRatings()

        viewModelScope.launch {
            uiModelLiveData.postValue(State.loading())
            try {
                service.getRatings(uid)
                    .collect { ratings ->
                        if (ratings.isEmpty()) {
                            uiModelLiveData.postValue(State.failed("Empty ratings array"))
                        } else {
                            mapRatings(ratings)

                            val uiModel = createUiModel()
                            uiModelLiveData.postValue(State.success(uiModel))
                        }
                    }
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
                uiModelLiveData.postValue(State.failed())
            }
        }
    }

    private fun resetRatings() {
        oneStarRatings = 0
        twoStarRatings = 0
        threeStarRatings = 0
        fourStarRatings = 0
        fiveStarRatings = 0
        totalNumberRatings = 0
    }

    private fun mapRatings(ratings: List<Int>) {
        for (rating in ratings) {
            when (rating) {
                1 -> oneStarRatings += 1
                2 -> twoStarRatings += 1
                3 -> threeStarRatings += 1
                4 -> fourStarRatings += 1
                5 -> fiveStarRatings += 1
            }
            totalNumberRatings += 1
        }
    }

    private fun createUiModel(): CoachRatingUiModel {
        val rating: Float =
            (5 * fiveStarRatings.toFloat() / totalNumberRatings.toFloat()) +
                    (4 * fourStarRatings.toFloat() / totalNumberRatings.toFloat()) +
                    (3 * threeStarRatings.toFloat() / totalNumberRatings.toFloat()) +
                    (2 * twoStarRatings.toFloat() / totalNumberRatings.toFloat()) +
                    (1 * oneStarRatings.toFloat() / totalNumberRatings.toFloat())
        return CoachRatingUiModel(
            rating = rating,
            totalNumberRatings = totalNumberRatings,
            fiveStarProgress = MAX_PERCENTAGE_PROGRESS * fiveStarRatings / totalNumberRatings,
            fourStarProgress = MAX_PERCENTAGE_PROGRESS * fourStarRatings / totalNumberRatings,
            threeStarProgress = MAX_PERCENTAGE_PROGRESS * threeStarRatings / totalNumberRatings,
            twoStarProgress = MAX_PERCENTAGE_PROGRESS * twoStarRatings / totalNumberRatings,
            oneStarProgress = MAX_PERCENTAGE_PROGRESS * oneStarRatings / totalNumberRatings,
        )
    }
}