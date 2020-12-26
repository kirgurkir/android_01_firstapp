package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var nextId = 1
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            like = 999,
            likedByMe = false,
            share = 4096,
            view = 2_234_245
        ),
        Post(
            id = nextId++,
            author = "Топ пост",
            content = "Тут что-то есть, наверное",
            published = "22 мая в 18:16",
            like = 10,
            likedByMe = true,
            share = 999,
            view = 268
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            like = 999,
            likedByMe = false,
            share = 4,
            view = 2_234_245
        )
    )
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
            if(it.id != post.id) it else it.copy(content = post.content)
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
        posts = posts.map { if (it.id != id) it else it.copy(share = it.share + 10) }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}