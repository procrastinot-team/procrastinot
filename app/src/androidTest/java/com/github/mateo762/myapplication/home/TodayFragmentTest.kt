package com.github.mateo762.myapplication.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.home.fragments.TodayFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.github.mateo762.myapplication.room.HabitDao
import com.github.mateo762.myapplication.room.HabitImageDao
import com.github.mateo762.myapplication.room.HabitImageRepository
import com.github.mateo762.myapplication.room.HabitRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TodayFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var todayFragment: TodayFragment

    @Before
    fun setup() {
        hiltRule.inject()
        todayFragment = TodayFragment()
        // First, we mock the PostRepository
        val habitsDao = object : HabitDao {
            override fun getAll(): List<HabitEntity> {
                return listOf()
            }

            override fun insertAll(posts: List<HabitEntity>) {
                return
            }

            override fun insertOne(habit: HabitEntity) {
                return
            }

            override fun delete(post: HabitEntity) {
                return
            }
        }
        todayFragment.habitRepository = HabitRepository(habitsDao)

        val habitsImageDao = object : HabitImageDao {
            override fun getAll(): List<HabitImageEntity> {
                return listOf()
            }

            override fun cacheAll(habits: List<HabitImageEntity>) {
                return
            }
        }
        todayFragment.habitImageRepository = HabitImageRepository(habitsImageDao)
    }

    @Test
    fun testGetLocalHabits() {
        todayFragment.getLocalHabits()
    }

    @Test
    fun testGetLocalImages() {
        todayFragment.getLocalImages()
    }

    @Test
    fun testUpdateHabitsCache() {
        todayFragment.updateHabitsCache(mutableListOf())
    }

    @Test
    fun testUpdateImagesCache() {
        todayFragment.updateImagesCache(mutableListOf())
    }
}