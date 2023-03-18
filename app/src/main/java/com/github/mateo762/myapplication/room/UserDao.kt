package com.github.mateo762.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>

    @Insert
    fun insertAll(vararg users: UserEntity)

    @Delete
    fun delete(user: UserEntity)
}