package org.example.repository

import org.example.model.UserModel

interface UserRepository {

    suspend fun getAll(): List<UserModel>
    suspend fun getById(id: Long): UserModel?
    suspend fun getByIds(ids: Collection<Long>): List<UserModel>
    suspend fun getByUsername(username: String): UserModel?
    suspend fun save(item: UserModel): UserModel

    suspend fun updateUserPosts(listLikes: List<Long>) : UserModel
    suspend fun updateUserReposts(listLikes: List<Long>) : UserModel
    suspend fun updateLikes(listLikes: List<Long>) : UserModel
    suspend fun updateDislikes(listLikes: List<Long>) : UserModel
    suspend fun updateShared(listLikes: List<Long>) : UserModel

}