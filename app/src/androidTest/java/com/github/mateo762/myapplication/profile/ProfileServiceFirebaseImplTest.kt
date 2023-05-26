package com.github.mateo762.myapplication.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import java.time.DayOfWeek
import java.util.UUID

@ExperimentalCoroutinesApi
class ProfileServiceFirebaseImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private var userId: String = UUID.randomUUID().toString()
    private lateinit var db: FirebaseDatabase

    @Before
    fun setup() {
        db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)
    }

    @Test
    fun getHabitsImages() = runTest {
        //given
        val habitImage = HabitImageEntity(url = "https://images.stockfreeimages.com/402/sfixl/4025248.jpg")
        db.reference.child("users/${userId}/imagesPath").push().setValue(habitImage).await()
        val profileService = ProfileServiceFirebaseImpl(db.reference)
        var array: ArrayList<HabitImageEntity>? = null

        //when
        profileService.getHabitsImages(userId).collect {
            array = it
        }

        //then
        checkNotNull(array)
        assertEquals(array?.size, 1)
    }

    @Test
    fun getHabits() = runTest {
        //given
        val habit1 = HabitEntity(
            name = "Play guitar",
            days = listOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
            startTime = "00:01",
            endTime = "23:58",
        )
        db.reference.child("users/${userId}/habitsPath").push().setValue(habit1).await()
        val profileService = ProfileServiceFirebaseImpl(db.reference)
        var array: ArrayList<HabitEntity>? = null

        //when
        profileService.getHabits(userId).collect {
            array = it
        }

        //then
        checkNotNull(array)
        assertEquals(array?.size, 1)
    }
}