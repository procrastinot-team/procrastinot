package com.github.mateo762.myapplication.username

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mateo762.myapplication.util.State
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Username ViewModel.
 */
@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val usernameService: UsernameService,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    companion object {
        private val TAG = UsernameViewModel::class.java.simpleName
    }

    var isUsernameTaken = MutableLiveData<State<Boolean>>()
    var postUsernameLiveData = MutableLiveData<State<Unit>>()

    /**
     * Checks whether a particular username is available.
     */
    fun isUsernameAvailable(chosenUsername: String) {
        viewModelScope.launch {
            try {
                usernameService.getUsernames()
                    .collect { usernames ->
                        var isUsernameAvailable = true
                        for (username in usernames) {
                            if (chosenUsername == username) {
                                isUsernameTaken.postValue(State.Success(true))
                                isUsernameAvailable = false
                                break
                            }
                        }
                        if (isUsernameAvailable) {
                            isUsernameTaken.postValue(State.Success(false))
                        }
                    }
            } catch (exception: Exception) {
                Log.d(TAG, exception.message.toString())
                isUsernameTaken.postValue(State.failed())
            }
        }
    }

    /**
     * Posts the username that was picked to Firebase.
     */
    fun pickUsername(username: String, oldUsername: String? = null) {
        firebaseAuth.currentUser?.let { user ->
            postUsernameLiveData.postValue(State.loading())
            var combined: Flow<List<Unit>>? = null
            try {
                combined = combine(
                    usernameService.postUsernameToUsernames(username, user.uid),
                    usernameService.postUsernameToUser(username, user.uid)
                ) { a, b ->
                    listOf(a, b)
                }
            } catch (exception: Exception) {
                Log.d(TAG, exception.message.toString())
                postUsernameLiveData.postValue(State.failed())
            }

            viewModelScope.launch {
                combined?.collect {
                    postUsernameLiveData.postValue(State.success(Unit))
                }
            }

            oldUsername?.let {
                usernameService.deleteUsername(it)
            }
        }
    }
}