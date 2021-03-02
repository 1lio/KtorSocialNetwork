package org.example.model

data class PostModel(

    val id: Long,
    val author: String,
    val content: String,
    val created: Long,

    // Location
    val location: Location,

    // Likes
    val likedCount: Int = 0,
    val dislikedCount: Int = 0,
    val likedByMe: Int = 0,     // 1 like, -1 dislike, 0 nothing

    // Comment
    var commentsCount: Int = 0,
    var commentsByMe: Boolean = false,
    var comments: List<Comment>? = null,

    // Reposts
    val repostCount: Int = 0,
    val repostByMe: Boolean = false,

    // Share
    val sharedCount: Int = 0,
    val sharedByMe: Boolean = false,

    // Content
    val video: Video? = null,
    val adv: Adv? = null,

    // Others
    val countViews: Long = 0L,
    val isHidden: Boolean = false,
    val postType: PostType = PostType.POST
)