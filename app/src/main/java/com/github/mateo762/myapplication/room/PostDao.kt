package com.github.mateo762.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getAll(): List<PostEntity>

    @Insert
    fun insertAll(vararg posts: PostEntity)

    @Delete
    fun delete(post: PostEntity)
}