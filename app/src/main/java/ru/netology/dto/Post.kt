package ru.netology.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val authorAvatar: String = "netology.jpg",
    val published: String,
    val likes: Int,
    val likedByMe: Boolean = false,
    val share: Int
)

