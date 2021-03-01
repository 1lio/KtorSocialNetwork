package org.example.model

data class PostModel(

    val id: Long,
    val author: String,
    val content: String,
    val created: Long,

    var likedByMe: Int,     // 1 like, -1 dislike, 0 nothing

    var likedCount: Int,
    var dislikedCount: Int,

    var sharedByMe: Boolean,
    var sharedCount: Int,

    var commentsByMe: Boolean,
    var commentsCount: Int,

    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,

    var videoUrl: String? = null,

    var postType: PostType = PostType.POST,
    var countViews: Int = 0,                 // Кол-во просмотров
)

enum class PostType {
    POST, REPOST, EVENT, VIDEO
}
