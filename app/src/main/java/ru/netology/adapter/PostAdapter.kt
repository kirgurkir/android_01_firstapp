package ru.netology.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post){}
    fun onShare(post: Post){}
    fun onEdit(post: Post){}
    fun onRemove(post: Post){}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            authorAvatarImageView.setImageResource(R.drawable.post_avatar)
            authorView.text = post.author
            publishedView.text = post.published
            contentView.text = post.content
            likeBtnView.isChecked = post.likedByMe
            likeBtnView.text = countToString(post.like)
            shareBtnView.text = countToString(post.share)
            viewCountView.text = countToString(post.view)

            menuButtonView.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menuActionRemove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.menuActionEdit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            likeBtnView.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shareBtnView.setOnClickListener {
                onInteractionListener.onShare(post)
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