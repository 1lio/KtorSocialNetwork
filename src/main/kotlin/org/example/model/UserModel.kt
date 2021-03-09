package org.example.model

import io.ktor.auth.Principal

data class UserModel(
    val id: Long = 0,
    val username: String,
    val password: String,

    // Храним в мапах Ключ id пользователя list id шники постов
    val createdPosts: Map<Long, List<Long>> = mapOf(),
    val likedPosts: Map<Long, List<Long>> = mapOf(),
    val dislikedPosts: Map<Long, List<Long>> = mapOf(),
    val repostedPost: Map<Long, List<Long>> = mapOf(),
    val sharedPosts: Map<Long, List<Long>> = mapOf()

) : Principal