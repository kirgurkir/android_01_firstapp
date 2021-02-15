package ru.netology.dao

import ru.netology.dto.Post

interface PostDao {

    fun getAll(): List<Post>
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post): Post
}