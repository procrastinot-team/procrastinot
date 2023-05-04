package com.github.mateo762.myapplication.room

import com.github.mateo762.myapplication.models.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(private val postDao: PostDao) {
    suspend fun getAllPosts(): List<PostEntity> {
        return withContext(Dispatchers.IO) {
            postDao.getAll()
        }
    }

    suspend fun insertAllPosts(posts: List<PostEntity>) {
        withContext(Dispatchers.IO) {
            postDao.insertAll(posts)
        }
    }
}
