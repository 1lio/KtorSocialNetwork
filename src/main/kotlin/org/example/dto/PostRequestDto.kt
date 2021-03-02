package org.example.dto

import org.example.model.Adv
import org.example.model.Location
import org.example.model.PostType
import org.example.model.Video

data class PostRequestDto(
    val id: Long,
    val author: String?,
    val content: String?,
    val created: Long?,
    val location: Location?,
    val videoUrl: Video?,
    val advUrl: Adv?,
    val postType: PostType?
)