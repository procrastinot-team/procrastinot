package com.github.mateo762.myapplication.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mateo762.myapplication.followers.UserRepository
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.username.UsernameViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val service: ProfileService,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    companion object {
        private val TAG = UsernameViewModel::class.java.simpleName
    }

    private val names = mutableListOf<String>()
    private val ids = mutableListOf<String>()
    private val daysList = mutableListOf<List<DayOfWeek>>()
    private val starts = mutableListOf<String>()
    private val ends = mutableListOf<String>()
    private var daysCount: ArrayList<Int> = ArrayList()
    private var averageRepetitionsPerWeek: Int = 0

    var habitImagesLiveData = MutableLiveData<ArrayList<HabitImageEntity>>()
    var habitLiveData = MutableLiveData<ArrayList<HabitEntity>>()
    var statsLiveData = MutableLiveData<ProfileStatsUiModel>()
    var userInfoLiveData = MutableLiveData<ProfileUserInfoUiModel>()
    var followingLiveData = MutableLiveData<Int>()
    var followersLiveData = MutableLiveData<Int>()

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser(uid)
                user?.let {
                    val userInfoUiModel = ProfileUserInfoUiModel(
                        name = user.name,
                        username = user.username,
                        email = user.email,
                        url = user.url
                    )
                    userInfoLiveData.postValue(userInfoUiModel)
                }
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    fun getHabitImages(uid: String) {
        viewModelScope.launch {
            try {
                service.getHabitsImages(uid)
                    .collect { habitImages ->
                        habitImagesLiveData.postValue(habitImages)
                    }
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    fun getHabits(uid: String) {
        viewModelScope.launch {
            try {
                service.getHabits(uid)
                    .collect { habits ->
                        habitLiveData.postValue(habits)
                        calculateStatistics(habits)
                    }
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    fun getFollowingNumber(uid: String) {
        viewModelScope.launch {
            try {
                var followingList = userRepository.getFollowing(uid)
                followingLiveData.postValue(followingList.size)
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    fun getFollowersNumber(uid: String) {
        viewModelScope.launch {
            try {
                var followersList = userRepository.getFollowers(uid)
                followersLiveData.postValue(followersList.size)
            } catch (exception: Exception) {
                Log.d(TAG, exception.toString())
            }
        }
    }

    private fun calculateStatistics(habits: ArrayList<HabitEntity>) {
        for (habit in habits) {
            names.add(habit.name)
            ids.add(habit.id)
            starts.add(habit.startTime)
            ends.add(habit.endTime)
            daysList.add(habit.days)
            daysCount.add(habit.days.size)
        }

        averageRepetitionsPerWeek = if (daysCount.size > 0) {
            daysCount.sum() / daysCount.size
        } else  {
            0
        }

        val statsUiModel = ProfileStatsUiModel(
            totalNumberOfHabits = habits.size,
            averageDaysInWeek = averageRepetitionsPerWeek,
            earliestStart = getEarliestStart(),
            latestEnd = getLatestEnd()
        )
        statsLiveData.postValue(statsUiModel)
    }

    private fun getLatestEnd(): String {
        if (ends.size > 0) {
            var latestHour = ends[0].split(":")[0].toInt()
            var latestMinute = ends[0].split(":")[1].toInt()
            for (i in 1 until ends.size) {
                val hour = ends[i].split(":")[0].toInt()
                val minute = ends[i].split(":")[1].toInt()
                if (hour > latestHour || (hour == latestHour && minute > latestMinute)) {
                    latestHour = hour
                    latestMinute = minute
                }
            }
            return "${latestHour}:${latestMinute}"
        } else {
            return "0"
        }
    }

    private fun getEarliestStart(): String {
        if (starts.size > 0) {
            var earliestHour = starts[0].split(":")[0].toInt()
            var earliestMinute = starts[0].split(":")[1].toInt()
            for (i in 1 until starts.size) {
                val hour = starts[i].split(":")[0].toInt()
                val minute = starts[i].split(":")[1].toInt()
                if (hour < earliestHour || (hour == earliestHour && minute < earliestMinute)) {
                    earliestHour = hour
                    earliestMinute = minute
                }
            }
            return "${earliestHour}:${earliestMinute}"
        } else {
            return "0"
        }
    }
}