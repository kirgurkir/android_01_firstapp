package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val postsList: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        val oldPosts = _data.value?.posts ?: mutableListOf()
        _data.value = FeedModel(loading = true)

        edited.value?.let {
            repository.saveByIdAsync(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    var isNew = true
                    val newPostsList: MutableList<Post> = mutableListOf()

                    oldPosts.forEach { _post ->
                        if (_post.id == post.id) {
                            isNew = false
                            newPostsList.add(post)
                        } else newPostsList.add(_post)
                    }

                    if (isNew) newPostsList.add(post)

                    _data.postValue(FeedModel(posts = newPostsList.toList()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(errorSave = true, posts = oldPosts))
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String, videoUrl: String?) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long, likeByMe: Boolean) {
        val status = postsList.value?.posts.orEmpty()
            .filter {
                it.id == id
            }.none { it.likedByMe }

        if (status) {
            repository.likeByIdAsync(id, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty()
                            .map {
                                if (it.id == post.id) post
                                else it
                            }
                        )
                    )
                }

                override fun onError(e: Exception) {
                    //_data.postValue(FeedModel(error = true))
                    _data.postValue(FeedModel(errorLikeDislike = true))
                }
            })
        } else {
            repository.unlikeByIdAsync(id, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty()
                            .map {
                                if (it.id == post.id) post
                                else it
                            }
                        )
                    )
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(errorLikeDislike = true))
                }
            })
        }
    }

    fun shareById(id: Long) {
        //TODO А нет на сервере реализации этого функционала
        /*thread { repository.shareById(id) }*/
    }

    fun removeById(id: Long) {
        val oldPosts = _data.value?.posts ?: mutableListOf()
        _data.value = FeedModel(loading = true)

        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = oldPosts
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun resetFeedModel(){
        val oldPosts = _data.value?.posts ?: mutableListOf()
        _data.value = FeedModel(posts = oldPosts)
    }
}

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0,
    share = 0
)