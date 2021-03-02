package org.example.dto

data class RepostRequestDto(
    val id: Int,
    val parentPostId: Int,
    val author: String,
    val content: String,
    val created: Long,
)