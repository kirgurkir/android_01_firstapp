package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        like = 999,
        likedByMe = false,
        share = 4096,
        view = 2_234_245
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        var newLikeCount = post.like

        if (post.likedByMe) newLikeCount-- else newLikeCount++

        post = post.copy(likedByMe = !post.likedByMe, like = newLikeCount)
        data.value = post
    }

    override fun share() {
        post = post.copy(share = post.share + 10)
        data.value = post
    }
}