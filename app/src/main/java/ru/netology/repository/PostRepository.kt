package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {

    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: Callback<Post>)
    fun unlikeByIdAsync(id: Long, callback: Callback<Post>)
    fun saveByIdAsync(post: Post, callback: Callback<Post>)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}