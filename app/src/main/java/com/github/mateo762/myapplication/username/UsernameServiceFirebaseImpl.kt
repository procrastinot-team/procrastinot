package com.github.mateo762.myapplication.username

import android.util.Log
import androidx.collection.ArraySet
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firebase implementation of the username service.
 */
class UsernameServiceFirebaseImpl @Inject constructor(
    private val db: DatabaseReference,
) : UsernameService {

    private var usernameList = ArraySet<String>()

    companion object {
        private val TAG = UsernameServiceFirebaseImpl::class.java.simpleName
        private const val USERNAMES_REF = "usernames"
        private const val USERNAME_REF = "username"
        private const val USERS_REF = "users"
    }

    override fun getUsernames() = flow {
        val snapshot = db.child(USERNAMES_REF).get().await()
        for (dataSnapshotChild in snapshot.children) {
            val usernameKey = dataSnapshotChild.key
            if (usernameKey != null) {
                usernameList.add(usernameKey)
            }
        }
        Log.d(TAG, usernameList.toString())
        emit(usernameList)
    }.flowOn(Dispatchers.IO)

    override fun postUsernameToUsernames(username: String, uid: String): Flow<Unit> = flow {
        db.child(USERNAMES_REF).child(username).setValue(uid).await()
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun postUsernameToUser(username: String, uid: String): Flow<Unit> = flow {
        db.child(USERS_REF).child(uid).child(USERNAME_REF).setValue(username).await()
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun deleteUsername(username: String) {
        db.child(USERNAMES_REF).child(username).removeValue()
    }
}