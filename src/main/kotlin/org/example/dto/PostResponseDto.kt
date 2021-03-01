package org.example.dto

import org.example.model.PostModel
import org.example.model.PostType

data class PostResponseDto(
    var id: Long,
    var author: String,
    var content: String,
    var created: Long,
    var likedByMe: Int,
    var likedCount: Int,
    var sharedByMe: Boolean,
    var sharedCount: Int,
    var commentsByMe: Boolean,
    var commentsCount: Int,
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var postType: PostType,
    var videoUrl: String? = null,
) {
    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            content = model.content,
            created = model.created,
            likedByMe = model.likedByMe,
            likedCount = model.likedCount,
            sharedByMe = model.sharedByMe,
            sharedCount = model.sharedCount,
            commentsByMe = model.commentsByMe,
            commentsCount = model.commentsCount,
            address = model.address,
            lat = model.lat,
            lng = model.lng,
            postType = model.postType,
            videoUrl = model.videoUrl
        )
    }
}