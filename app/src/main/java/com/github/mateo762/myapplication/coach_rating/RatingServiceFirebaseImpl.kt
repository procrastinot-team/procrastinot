package com.github.mateo762.myapplication.coach_rating

import android.util.Log
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firebase implementation of the RatingService.
 */
class RatingServiceFirebaseImpl @Inject constructor(private val db: DatabaseReference) :
    RatingService {

    companion object {
        private val TAG = RatingServiceFirebaseImpl::class.java.simpleName
        const val RATINGS_REF = "ratings"
    }

    override fun getRatings(uid: String): Flow<List<Int>> = flow {
        val array = ArrayList<Int>()
        val snapshot = db.child(RATINGS_REF).child(uid).get().await()

        for (child in snapshot.children) {
            val value = child.getValue(Int::class.java)
            value?.let {
                array.add(it)
            }
        }

        Log.d(TAG, array.toString())
        emit(array)
    }.flowOn(Dispatchers.IO)
}
