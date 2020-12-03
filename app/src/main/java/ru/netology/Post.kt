package ru.netology

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var like: Int,
    var likedByMe: Boolean = false,
    var share: Int,
    val view: Int
)

