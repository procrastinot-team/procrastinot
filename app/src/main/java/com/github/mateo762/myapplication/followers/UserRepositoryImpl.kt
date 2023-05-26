package com.github.mateo762.myapplication.followers

import android.util.Log
import com.github.mateo762.myapplication.models.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepositoryImpl @Inject constructor(private val db: DatabaseReference, private val auth: FirebaseAuth) : UserRepository {

    companion object {
        private val TAG = UserRepositoryImpl::class.java.simpleName
    }

    private var usersReference: DatabaseReference = db.child("users")
    override fun getUserUid(): String {
        return auth.currentUser?.uid ?: ""
    }

    override suspend fun getUser(uid: String): UserEntity? {
        return suspendCancellableCoroutine { continuation ->
            usersReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserEntity::class.java)
                    continuation.resume(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.toException().toString())
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    override suspend fun followUser(currentUserId: String, targetUserId: String) {
        val currentUser = getUser(currentUserId)
        val targetUser = getUser(targetUserId)

        if (currentUser != null) {
            val updatedFollowingList =
                currentUser.followingUsers.toMutableList().apply { add(targetUserId) }
            usersReference.child(currentUserId).child("followingPath").push()
                .setValue(updatedFollowingList).await()
        }

        if (targetUser != null) {
            val updatedFollowerList =
                targetUser.followerUsers.toMutableList().apply { add(currentUserId) }
            usersReference.child(targetUserId).child("followersPath").push()
                .setValue(updatedFollowerList).await()
        }
    }

    override fun unfollowUser(currentUserId: String, targetUserId: String) {
        usersReference.child(currentUserId).child("followingPath")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val followingList =
                            childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                        if (followingList?.contains(targetUserId) == true) {
                            val updatedFollowingList =
                                followingList.toMutableList().apply { remove(targetUserId) }
                            childSnapshot.ref.setValue(updatedFollowingList)
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.toException().toString())
                }
            })

        usersReference.child(targetUserId).child("followersPath")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val followerList =
                            childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                        if (followerList?.contains(currentUserId) == true) {
                            val updatedFollowerList =
                                followerList.toMutableList().apply { remove(currentUserId) }
                            childSnapshot.ref.setValue(updatedFollowerList)
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.toException().toString())
                }
            })
    }

    override suspend fun checkIfUserFollows(currentUserId: String, targetUserId: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            usersReference.child(currentUserId).child("followingPath")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var found = false
                        for (childSnapshot in snapshot.children) {
                            val followingList = childSnapshot.getValue(object :
                                GenericTypeIndicator<List<String>>() {})
                            if (followingList?.contains(targetUserId) == true) {
                                found = true
                                break
                            }
                        }
                        continuation.resume(found)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, error.toException().toString())
                        continuation.resumeWithException(error.toException())
                    }
                })
        }
    }

    override suspend fun getFollowers(currentUserId: String): List<String> {
        return suspendCancellableCoroutine { continuation ->
            usersReference.child(currentUserId).child("followersPath").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = ArrayList<String>()

                    for (child in snapshot.children) {
                        child?.let {
                            var childList = it.getValue(object : GenericTypeIndicator<List<String?>>() {})
                            list.addAll(childList as ArrayList<String>)
                        }
                    }
                    continuation.resume(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.toException().toString())
                }
            })
        }
    }

    override suspend fun getFollowing(currentUserId: String): List<String> {
        return suspendCancellableCoroutine { continuation ->
            usersReference.child(currentUserId).child("followingPath").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = ArrayList<String>()

                    for (child in snapshot.children) {
                        child?.let {
                            var childList = it.getValue(object : GenericTypeIndicator<List<String?>>() {})
                            list.addAll(childList as ArrayList<String>)
                        }
                    }
                    continuation.resume(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.toException().toString())
                }
            })
        }
    }
}