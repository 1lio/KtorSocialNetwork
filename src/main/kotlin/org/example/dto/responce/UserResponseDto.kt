package org.example.dto.responce

import org.example.model.UserModel

class UserResponseDto(
    val id: Long,
    val username: String,
    val createdPosts: Map<Long, List<Long>> = mapOf(),
    val likedPosts: Map<Long, List<Long>> = mapOf(),
    val dislikedPosts: Map<Long, List<Long>> = mapOf(),
    val repostedPost: Map<Long, List<Long>> = mapOf(),
    val sharedPosts: Map<Long, List<Long>> = mapOf(),
) {
    companion object {
        fun fromModel(model: UserModel) = UserResponseDto(
            id = model.id,
            username = model.username,
            createdPosts = model.createdPosts,
            likedPosts = model.likedPosts,
            dislikedPosts = model.dislikedPosts,
            repostedPost = model.repostedPost,
            sharedPosts = model.sharedPosts,
        )
    }
}