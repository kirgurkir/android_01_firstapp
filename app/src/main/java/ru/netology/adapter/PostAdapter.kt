package ru.netology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post

typealias OnLikeListener = (post: Post) -> Unit
typealias OnShareListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
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

            likeImageView.setOnClickListener {
                onLikeListener(post)
            }
            shareImageView.setOnClickListener {
                onShareListener(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
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