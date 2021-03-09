package org.example.model

data class PostModel(

    val id: Long,
    val authorId: Long,
    val content: String,
    val created: Long,
    val imageUrl: String,               // Прикрепленное изображение

    // Likes
    var likedCount: Int = 0,
    var dislikedCount: Int = 0,
    var likedByMe: Int = 0,             // 1 like, -1 dislike, 0 nothing

    // Reposts
    var repostCount: Int = 0,
    var repostByMe: Boolean = false,

    // Share
    var sharedCount: Int = 0,
    var sharedByMe: Boolean = false,

    // Прочие события
    val event: EventModel? = null,       // Событие подразумевает адрес и координаты
    val video: VideoModel? = null,       // Видеоконтент

    var postType: PostType = PostType.POST,
    var countViews: Int = 0,
)
