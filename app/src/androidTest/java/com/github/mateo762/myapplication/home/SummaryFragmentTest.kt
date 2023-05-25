package com.github.mateo762.myapplication.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mateo762.myapplication.home.fragments.SummaryFragment
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.room.HabitDao
import com.github.mateo762.myapplication.room.HabitRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SummaryFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    private lateinit var summaryFragment: SummaryFragment

    @Before
    fun setup() {
        hiltRule.inject()
        summaryFragment = SummaryFragment()
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
        summaryFragment.habitRepository = HabitRepository(habitsDao)
    }

    @Test
    fun testGetLocalHabits() {
        summaryFragment.getLocalHabits()
    }
    @Test
    fun testUpdateHabitsCache() {
        summaryFragment.updateHabitsCache(mutableListOf())
    }
}