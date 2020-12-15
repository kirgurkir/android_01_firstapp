package ru.netology.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
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

    fun cancelEdit(){
        edited.value = empty
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) return
            edited.value = it.copy(content = text)
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
    view = 0
)