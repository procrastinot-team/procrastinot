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
    var followingLiveData = MutableLiveData<Int>()
    var followersLiveData = MutableLiveData<Int>()

    fun getHabitImages() {
        viewModelScope.launch {
            auth.uid?.let { userId ->
                try {
                    service.getHabitsImages(userId)
                        .collect { habitImages ->
                            habitImagesLiveData.postValue(habitImages)
                        }
                } catch (exception: Exception) {
                    Log.d(TAG, exception.toString())
                }
            }
        }
    }

    fun getHabits() {
        viewModelScope.launch {
            auth.uid?.let { userId ->
                try {
                    service.getHabits(userId)
                        .collect { habits ->
                            habitLiveData.postValue(habits)
                            calculateStatistics(habits)
                        }
                } catch (exception: Exception) {
                    Log.d(TAG, exception.toString())
                }
            }
        }
    }

    fun getFollowingNumber() {
        viewModelScope.launch {
            auth.uid?.let {
                try {
                    var followingList = userRepository.getFollowing(it)
                    followingLiveData.postValue(followingList.size)
                } catch (exception: Exception) {
                    Log.d(TAG, exception.toString())
                }
            }
        }
    }

    fun getFollowersNumber() {
        viewModelScope.launch {
            auth.uid?.let {
                try {
                    var followersList = userRepository.getFollowers(it)
                    followersLiveData.postValue(followersList.size)
                } catch (exception: Exception) {
                    Log.d(TAG, exception.toString())
                }
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

        averageRepetitionsPerWeek = daysCount.sum() / daysCount.size

        val statsUiModel = ProfileStatsUiModel(
            totalNumberOfHabits = habits.size,
            averageDaysInWeek = averageRepetitionsPerWeek,
            earliestStart = getEarliestStart(),
            latestEnd = getLatestEnd()
        )
        statsLiveData.postValue(statsUiModel)
    }

    private fun getLatestEnd(): String {
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
    }

    private fun getEarliestStart(): String {
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
    }
}