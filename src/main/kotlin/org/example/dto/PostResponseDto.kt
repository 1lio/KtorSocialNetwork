package org.example.dto

import org.example.model.*

data class PostResponseDto(
    val id: Long,
    val author: String,
    val content: String,
    val created: Long,
    val location: Location,
    val likedCount: Int,
    val dislikedCount: Int,
    val likedByMe: Int,
    val commentsCount: Int,
    val commentsByMe: Boolean,
    val comments: List<Comment>?,
    val repostByMe: Boolean,
    val repostCount: Int,
    val sharedByMe: Boolean,
    val sharedCount: Int,
    val video: Video?,
    val adv: Adv?,
    val isHidden: Boolean,
    val countViews: Long,
    val postType: PostType
) {
    companion object {

        // В ответе мы отдаем модель как есть.
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            content = model.content,
            created = model.created,
            location = model.location,
            likedCount = model.likedCount,
            dislikedCount = model.dislikedCount,
            likedByMe = model.likedByMe,
            commentsCount = model.commentsCount,
            commentsByMe = model.commentsByMe,
            comments = model.comments,
            repostByMe = model.repostByMe,
            repostCount = model.repostCount,
            sharedByMe = model.sharedByMe,
            sharedCount = model.sharedCount,
            video = model.video,
            adv = model.adv,
            isHidden = model.isHidden,
            countViews = model.countViews,
            postType = model.postType
        )
    }
}