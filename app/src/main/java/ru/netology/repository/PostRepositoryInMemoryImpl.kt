package ru.netology.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post
import androidx.core.content.edit

class PostRepositoryInMemoryImpl(
    context: Context
) : PostRepository {

    private companion object {
        const val POST_FILE = "posts.json"
    }

    private var nextId = 1
    private val file = context.filesDir.resolve(POST_FILE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val gson = Gson()

    private var posts: List<Post> = file.exists().let { exists ->
        if (exists) {
            gson.fromJson(file.readText(), type)
        } else {
            emptyList()
        }
    }
    set(value) {
        field = value
        sync()
    }
    
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        if (post.id == 0) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content, videoUrl = post.videoUrl)
        }
        data.value = posts
    }

    override fun likeById(id: Int) {

        posts = posts.map {
            if (it.id != id) it
            else {
                var newLikeCount = it.like
                if (it.likedByMe) newLikeCount-- else newLikeCount++
                it.copy(likedByMe = !it.likedByMe, like = newLikeCount)
            }
        }
        data.value = posts
    }

    override fun shareById(id: Int) {
        posts = posts.map { if (it.id != id) it else it.copy(share = it.share + 1) }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    private fun sync() {
        file.writeText(gson.toJson(posts))
    }
}