package com.github.mateo762.myapplication.profile

import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileServiceFirebaseImpl @Inject constructor(
    private val db: DatabaseReference,
) : ProfileService {

    private lateinit var imageList: ArrayList<HabitImageEntity>
    private lateinit var habits: ArrayList<HabitEntity>

    override fun getHabitsImages(userId: String) = flow {
        imageList = ArrayList()
        val snapshot = db.child("users/${userId}/imagesPath").get().await()
        for (child in snapshot.children) {
            val image = child.getValue(HabitImageEntity::class.java)
            image?.let {
                imageList.add(it)
            }
        }
        emit(imageList)
    }.flowOn(Dispatchers.IO)

    override fun getHabits(userId: String) = flow {
        habits = ArrayList()
        val snapshot = db.child("users/${userId}/habitsPath").get().await()
        for (child in snapshot.children) {
            val habitEntity = child.getValue(HabitEntity::class.java)
            habitEntity?.let {
                habits.add(it)
            }
        }
        emit(habits)
    }.flowOn(Dispatchers.IO)
}