package com.github.mateo762.myapplication.coach_rating

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ViewCoachingRatingBinding
import com.github.mateo762.myapplication.util.State

/**
 * Custom view for the coach rating information.
 */
class CoachRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var viewModel: CoachRatingViewModel? = null

    private val binding =
        ViewCoachingRatingBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * Method that sets the view model and starts observing for live data updates.
     *
     * @param viewModel the CoachRatingViewModel.
     */
    fun setViewModel(viewModel: CoachRatingViewModel) {
        this.viewModel = viewModel
        viewModel.uiModelLiveData.observe(findViewTreeLifecycleOwner()!!, ::populateView)
    }

    /**
     * Method that gets the rating stats.
     */
    fun getRatingStats(uid: String) {
        viewModel?.getRatingStats(uid)
    }

    private fun populateView(state: State<CoachRatingUiModel>) {
        when (state) {
            is State.Failed -> {
                binding.infoTextView.visibility = View.VISIBLE
                binding.ratingContainer.visibility = View.GONE

                binding.infoTextView.text = context.getString(R.string.fetch_coach_rating_error)
            }

            is State.Loading -> {
                binding.infoTextView.visibility = View.VISIBLE
                binding.ratingContainer.visibility = View.GONE

                binding.infoTextView.text = context.getString(R.string.loading)
            }

            is State.Success -> {
                val uiModel = state.data

               setRatingViewSuccess(uiModel)
            }
        }
    }

    private fun setRatingViewSuccess(uiModel: CoachRatingUiModel) {
        binding.infoTextView.visibility = View.GONE
        binding.ratingContainer.visibility = View.VISIBLE

        binding.ratingBar.rating = uiModel.rating
        binding.ratingNumber.text = uiModel.rating.toString()
        binding.fiveStarsProgress.progress = uiModel.fiveStarProgress
        binding.fourStarsProgress.progress = uiModel.fourStarProgress
        binding.threeStarsProgress.progress = uiModel.threeStarProgress
        binding.twoStarsProgress.progress = uiModel.twoStarProgress
        binding.oneStarsProgress.progress = uiModel.oneStarProgress
        binding.totalRatings.text =
            context.getString(
                R.string.total_number_ratings,
                uiModel.totalNumberRatings.toString()
            )
    }
}