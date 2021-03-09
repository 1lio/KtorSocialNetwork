package org.example.dto.responce

import org.example.model.EventModel
import org.example.model.PostModel
import org.example.model.PostType
import org.example.model.VideoModel

data class PostResponseDto(
    val id: Long,
    val authorId: Long,
    val content: String,
    val created: Long,
    val imageUrl: String,
    var likedCount: Int = 0,
    var dislikedCount: Int = 0,
    var likedByMe: Int = 0,
    var repostCount: Int = 0,
    var repostByMe: Boolean = false,
    var sharedCount: Int = 0,
    var sharedByMe: Boolean = false,
    val event: EventModel? = null,
    val video: VideoModel? = null,
    var postType: PostType = PostType.POST,
    var countViews: Int,
) {
    companion object {
        fun fromModel(dto: PostModel) = PostResponseDto(
            id = dto.id,
            authorId = dto.authorId,
            content = dto.content,
            created = dto.created,
            imageUrl = dto.imageUrl,
            likedCount = dto.likedCount,
            dislikedCount = dto.dislikedCount,
            likedByMe = dto.likedByMe,
            sharedCount = dto.sharedCount,
            sharedByMe = dto.sharedByMe,
            event = dto.event,
            video = dto.video,
            postType = dto.postType,
            countViews = dto.countViews
        )
    }
}