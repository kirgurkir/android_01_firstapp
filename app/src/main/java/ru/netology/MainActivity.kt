package ru.netology

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            like = 999,
            likedByMe = false,
            share = 4096,
            view = 2_234_245
        )

        updatePost(binding, post)


        binding.likeImageView.setOnClickListener {
            if (post.likedByMe) post.like-- else post.like++
            post.likedByMe = !post.likedByMe

            updatePost(binding, post)
        }

        binding.shareCountView.setOnClickListener {
            post.share += 10

            updatePost(binding, post)
        }

    }
}

fun countToString(count: Int): String {
    var res = count.toString()

    if (count in 1_000..999999) {
        res = (count / 1_000).toString()

        val decimal = count % 1_000 / 100
        if (decimal > 0) res += ".$decimal"

        res += "K"

    } else if (count >= 1_000_000) {
        res = (count / 1_000_000).toString()

        val decimal = count % 1_000_000 / 100_000
        if (decimal > 0) res += ".$decimal"

        res += "M"
    }

    return res
}

fun updatePost(binding: ActivityMainBinding, post: Post) {
    with(binding) {
        authorAvatarImageView.setImageResource(R.drawable.post_avatar)
        authorView.text = post.author
        publishedView.text = post.published
        contentView.text = post.content
        likeImageView.setImageResource(
            if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_outline_favorite_border_24
        )
        likeCountView.text = countToString(post.like)
        shareCountView.text = countToString(post.share)
        viewCountView.text = countToString(post.view)
    }
}