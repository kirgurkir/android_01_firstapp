package ru.netology.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val post = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
}