package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.model.dao.PostDao
import ru.netology.model.dto.Post
import ru.netology.model.entity.PostEntity

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(dao.getAll()) { list ->
        list.map { entity ->
            entity.toDto()
        }
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
    }
}