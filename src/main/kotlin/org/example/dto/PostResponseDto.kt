package org.example.dto

import org.example.model.PostModel
import org.example.model.PostType

data class PostResponseDto(
    var id: Long,
    var author: String,
    var content: String,
    var dateStamp: Long,
    var likedByMe: Boolean,
    var likedCount: Int,
    var sharedByMe: Boolean,
    var sharedCount: Int,
    var commentsByMe: Boolean,
    var commentsCount: Int,
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var postType: PostType,
    var videoLink: String? = null,
    var intentLink: String? = null,
    var imageLink: Int? = null
) {
    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            content = model.content,
            dateStamp = model.dateStamp,
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
            videoLink = model.videoLink,
            intentLink = model.intentLink,
            imageLink = model.imageLink
        )
    }
}