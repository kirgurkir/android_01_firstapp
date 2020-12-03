package ru.netology.dto

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val like: Int,
    val likedByMe: Boolean = false,
    val share: Int,
    val view: Int
)

