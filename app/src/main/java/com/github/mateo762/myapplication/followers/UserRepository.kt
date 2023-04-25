package com.github.mateo762.myapplication.followers

import android.content.ContentValues.TAG
import android.util.Log
import com.github.mateo762.myapplication.profile.ProfileActivity
import com.github.mateo762.myapplication.room.UserEntity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.*

class UserRepository {
    private val database = FirebaseDatabase.getInstance()
    private val usersReference = database.getReference("users")

    private fun getUser(uid: String, callback: (UserEntity?) -> Unit) {
        usersReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserEntity::class.java)
                callback(user)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    fun followUser(currentUserId: String, targetUserId: String) {
        getUser(currentUserId) { currentUser ->
            if (currentUser != null) {
                val updatedFollowingList = currentUser.followingUsers.toMutableList().apply { add(targetUserId) }
                usersReference.child(currentUserId).child("followingPath").push().setValue(updatedFollowingList)
            }
        }

        getUser(targetUserId) { targetUser ->
            if (targetUser != null) {
                val updatedFollowerList = targetUser.followerUsers.toMutableList().apply { add(currentUserId) }
                usersReference.child(targetUserId).child("followersPath").push().setValue(updatedFollowerList)
            }
        }
    }

    fun unfollowUser(currentUserId: String, targetUserId: String) {
        usersReference.child(currentUserId).child("followingPath")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val followingList = childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                        if (followingList?.contains(targetUserId) == true) {
                            val updatedFollowingList = followingList.toMutableList().apply { remove(targetUserId) }
                            childSnapshot.ref.setValue(updatedFollowingList)
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        usersReference.child(targetUserId).child("followersPath")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val followerList = childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                        if (followerList?.contains(currentUserId) == true) {
                            val updatedFollowerList = followerList.toMutableList().apply { remove(currentUserId) }
                            childSnapshot.ref.setValue(updatedFollowerList)
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    fun checkIfUserFollows(currentUserId: String, targetUserId: String): Task<Boolean> {
        val taskCompletionSource = TaskCompletionSource<Boolean>()

        usersReference.child(currentUserId).child("followingPath")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var found = false
                    for (childSnapshot in snapshot.children) {
                        val followingList = childSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                        if (followingList?.contains(targetUserId) == true) {
                            found = true
                            break
                        }
                    }
                    taskCompletionSource.setResult(found)
                }

                override fun onCancelled(error: DatabaseError) {
                    taskCompletionSource.setResult(false)
                }
            })

        return taskCompletionSource.task
    }
}