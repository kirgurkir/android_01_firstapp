package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {

    fun getAllAsync(callback: GetAllCallback)
    fun likeByIdAsync(callback: PostCallback, id: Long)
    fun unlikeByIdAsync(callback: PostCallback, id: Long)
    fun saveByIdAsync(callback: PostCallback, post: Post)
    fun removeByIdAsync(callback: RemovePostCallback, id: Long)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface PostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }

    interface RemovePostCallback {
        fun onSuccess(id: Long) {}
        fun onError(e: Exception) {}
    }
}