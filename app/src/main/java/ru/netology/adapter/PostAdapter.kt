package ru.netology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onPost(post: Post) {}
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
            val url = "http://192.168.90.200:9999/avatars/${post.authorAvatar}"
            authorAvatarImageView.load(url)
            authorView.text = post.author
            publishedView.text = post.published
            contentView.text = post.content
            likeBtnView.isChecked = post.likedByMe
            likeBtnView.text = countToString(post.likes)
            shareBtnView.text = countToString(post.share)
            //viewCountView.text = countToString(post.view)

            /*if (!post.videoUrl.isNullOrBlank()) {
                videoView.visibility = View.VISIBLE
                videoView.setOnClickListener {
                    onInteractionListener.onVideo(post)
                }
            }*/

            containerView.setOnClickListener {
                onInteractionListener.onPost(post)
            }

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

inline fun ImageView.load(url: String, init: RequestBuilder<*>.() -> Unit = {}) {
    Glide.with(this)
        .load(url)
        .also(init)
        .timeout(30_000)
        .placeholder(R.color.netologyGreen)
        .error(R.drawable.ic_baseline_error_24)
        .transform(RoundedCorners(100))
        .into(this)
}
