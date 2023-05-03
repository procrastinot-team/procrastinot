package com.github.mateo762.myapplication.habits

import android.util.Log
import com.github.mateo762.myapplication.models.HabitEntity
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class HabitServiceFirebaseImpl @Inject constructor(
    private val db: DatabaseReference
) : HabitService {
    private var TAG = HabitServiceFirebaseImpl::class.java.simpleName

    override fun addHabit(user: String, habit: HabitEntity, callback: HabitServiceCallback) {
        Log.d(TAG, "Hello database")
        //val db: DatabaseReference = Firebase.database.reference
        // makfazlic should be replaced with the userId retrieved from the auth
        val userRef = db.child("users").child(user)
        val key = userRef.push().key
        if (key != null) {
            db.child("users").child(user).child(key).setValue(habit).addOnSuccessListener {
                callback.onSuccess()
            }.addOnFailureListener {
                callback.onFailure()
            }
        }
    }

    override fun getHabits(user: String, callback: HabitServiceCallback) {
        // fetch for the count of habits before the creation of habit
        val userRefBefore = db.child("users").child("makfazlic")
        userRefBefore.get().addOnSuccessListener {
            val resp: HashMap<String, Any> = it.value as HashMap<String, Any>
            Log.i(TAG, "Got value ${resp.size}")
        }.addOnFailureListener{
            Log.e(TAG, "Error getting data", it)
        }

    }
}