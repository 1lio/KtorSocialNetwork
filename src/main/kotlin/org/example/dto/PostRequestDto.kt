package org.example.dto

import org.example.model.PostType

data class PostRequestDto(
    var id: Long,
    var author: String?,
    var content: String?,
    var date: Long?,
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    var likedCount: Int = 0,
    var dislikedCount: Int = 0,
    var likedByMe: Int = 0,
    var repostByMe: Boolean = false,
    var repostCount: Int = 0,
    var sharedCount: Int = 0,
    var sharedByMe: Boolean = false,
    var videoUrl: String? = null,
    var advUrl: String? = null,
    var countViews: Int = 0,
    var postType: PostType = PostType.POST
)