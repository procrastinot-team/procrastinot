package com.github.mateo762.myapplication.followers

import com.github.mateo762.myapplication.room.UserEntity

interface IUserRepository {
    suspend fun getUser(uid: String): UserEntity?
    suspend fun followUser(currentUserId: String, targetUserId: String)
    suspend fun unfollowUser(currentUserId: String, targetUserId: String)
    suspend fun checkIfUserFollows(currentUserId: String, targetUserId: String): Boolean
}