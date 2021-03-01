package org.example.repository

import org.example.model.PostModel

interface PostRepository {
    suspend fun getAll(): List<PostModel>
    suspend fun getById(id: Long): PostModel?
    suspend fun save(item: PostModel): PostModel
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long): PostModel?

    suspend fun dislikeById(id: Long): PostModel?
    suspend fun repost(id: Long): PostModel?
    suspend fun share(id: Long): PostModel?
}