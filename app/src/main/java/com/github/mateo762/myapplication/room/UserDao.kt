package com.github.mateo762.myapplication.room

import androidx.room.*
import com.github.mateo762.myapplication.models.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM userEntity WHERE userEntity.username LIKE :queryUsername")
    fun getByUsername(queryUsername: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg users: UserEntity)

    @Update
    fun update(users: UserEntity)
}