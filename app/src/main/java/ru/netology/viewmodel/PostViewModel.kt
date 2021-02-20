package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.model.db.AppDb
import ru.netology.model.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositorySQLiteImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    // упрощённый вариант
    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao()
    )
    val postsList = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String, videoUrl: String?) {
        edited.value?.let {
            val text = content.trim()
            val url = videoUrl?.trim()
            if (it.content == text && it.videoUrl == url) return
            edited.value = it.copy(content = text, videoUrl = videoUrl)
        }
    }

    fun likeById(id: Int) = repository.likeById(id)
    fun shareById(id: Int) = repository.shareById(id)
    fun removeById(id: Int) = repository.removeById(id)
}

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    like = 0,
    share = 0,
    view = 0,
    videoUrl = ""
)