package ru.netology.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import ru.netology.adapter.OnLikeListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val likeListener: (post: Post) -> Unit = { viewModel.likeById(it.id) }
        val shareListener: (post: Post) -> Unit = { viewModel.shareById(it.id) }
        val adapter = PostsAdapter(likeListener, shareListener)

        binding.rvPostsView.adapter = adapter
        viewModel.postsList.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}