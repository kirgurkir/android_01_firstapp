package ru.netology.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.model.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val like: Int,
    val share: Int,
    val view: Int,
    val videoUrl: String?,
) {
    fun toDto() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        like = like,
        share = share,
        view = view,
        videoUrl = videoUrl
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.like, dto.share, dto.view, dto.videoUrl)

    }
}