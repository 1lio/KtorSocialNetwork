package org.example.dto

import org.example.model.PostType

data class PostRequestDto(
    var id: Long,
    var author: String,
    var content: String,
    var dateStamp: Long,
    var likedByMe: Int,
    var likedCount: Int,
    var dislikedCount: Int,

    var sharedByMe: Boolean,
    var sharedCount: Int,
    var commentsByMe: Boolean,
    var commentsCount: Int,
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var postType: PostType = PostType.POST,
    var videoUrl: String? = null,
)