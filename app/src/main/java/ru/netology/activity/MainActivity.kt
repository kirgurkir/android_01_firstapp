package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import ru.netology.R
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.post.observe(this) { post ->
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

        binding.likeImageView.setOnClickListener {
            viewModel.like()
        }

        binding.shareImageView.setOnClickListener {
            viewModel.share()
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