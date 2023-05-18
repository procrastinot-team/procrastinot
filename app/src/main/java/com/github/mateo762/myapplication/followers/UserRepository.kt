package com.github.mateo762.myapplication.followers

import com.github.mateo762.myapplication.models.UserEntity

interface UserRepository {

    fun getUserUid(): String

    suspend fun getUser(uid: String): UserEntity?

    suspend fun followUser(currentUserId: String, targetUserId: String)

    fun unfollowUser(currentUserId: String, targetUserId: String)

    suspend fun checkIfUserFollows(currentUserId: String, targetUserId: String): Boolean

    suspend fun getFollowers(currentUserId: String): List<String>

    suspend fun getFollowing(currentUserId: String): List<String>

}