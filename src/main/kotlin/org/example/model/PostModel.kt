package org.example.model

data class PostModel(
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
)

enum class PostType {
    POST, REPOST
}