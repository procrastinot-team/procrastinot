package com.github.mateo762.myapplication.room

import androidx.room.*
import com.github.mateo762.myapplication.models.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM postEntity")
    fun getAll(): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(posts: List<PostEntity>)

    @Delete
    fun delete(post: PostEntity)
}