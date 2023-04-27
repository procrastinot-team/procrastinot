package com.github.mateo762.myapplication.username

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ActivityUsernameBinding
import com.github.mateo762.myapplication.home.HomeActivity
import com.github.mateo762.myapplication.notifications.NotificationInfoActivity
import com.github.mateo762.myapplication.util.State
import com.github.mateo762.myapplication.util.showToast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity for picking a unique username.
 */
@AndroidEntryPoint
class UsernameActivity : BaseActivity() {

    private val viewModel: UsernameViewModel by viewModels()
    private lateinit var notificationManager: NotificationManager
    private lateinit var binding: ActivityUsernameBinding

    private val usernameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //no-operation
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            text?.let {
                if (it.length >= 5) {
                    viewModel.isUsernameAvailable(it.toString())
                } else {
                    binding.continueButton.isEnabled = false
                    setUsernameFeedbackState(
                        R.string.choose_username_feedback_minimum_characters,
                        R.color.red,
                        false
                    )
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //no-operation
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        binding.username.addTextChangedListener(usernameTextWatcher)

        viewModel.isUsernameTaken.observe(this) { state ->
            handleIsUsernameTakenState(state)
        }

        viewModel.postUsernameLiveData.observe(this) { state ->
            handlePostUsernameLiveDataState(state)
        }

        binding.continueButton.setOnClickListener {
            val chosenUsername = binding.username.text
            if (chosenUsername.isNullOrBlank()) {
                showToast(R.string.choose_username_empty_username_message)
            } else {
                viewModel.pickUsername(chosenUsername.toString())
            }
        }
    }

    private fun setUsernameFeedbackState(
        @StringRes feedbackTextRes: Int,
        @ColorRes feedbackTextColor: Int,
        isContinueButtonEnabled: Boolean
    ) {
        binding.usernameFeedback.text =
            getString(feedbackTextRes)
        binding.usernameFeedback.setTextColor(
            ContextCompat.getColor(
                this,
                feedbackTextColor
            )
        )
        binding.continueButton.isEnabled = isContinueButtonEnabled
    }

    private fun showProgress(show: Boolean) {
        binding.loadingContainer.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun handleIsUsernameTakenState(state: State<Boolean>) {
        when (state) {
            is State.Failed -> {
                setUsernameFeedbackState(
                    R.string.choose_username_check_username_error,
                    R.color.red,
                    false
                )
            }
            is State.Loading -> {
                setUsernameFeedbackState(
                    R.string.choose_username_feedback_username_loading,
                    R.color.black,
                    false
                )
            }
            is State.Success -> {
                val isUsernameTaken = state.data
                if (isUsernameTaken) {
                    setUsernameFeedbackState(
                        R.string.choose_username_feedback_username_taken,
                        R.color.red,
                        false
                    )
                } else {
                    setUsernameFeedbackState(
                        R.string.choose_username_feedback_username_available,
                        R.color.green,
                        true
                    )
                }
            }
        }
    }

    private fun handlePostUsernameLiveDataState(state: State<Unit>) {
        when (state) {
            is State.Failed -> {
                showProgress(false)
                showToast(R.string.choose_username_pick_username_error)
            }
            is State.Loading -> {
                showProgress(true)
            }
            is State.Success -> {
                showProgress(false)
                showToast(R.string.choose_username_pick_username_success)
                val intent: Intent =
                    if (this.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) || !notificationManager.areNotificationsEnabled()) {
                        Intent(this, NotificationInfoActivity::class.java)
                    } else {
                        Intent(this, HomeActivity::class.java)
                    }
                startActivity(intent)
            }
        }
    }
}
