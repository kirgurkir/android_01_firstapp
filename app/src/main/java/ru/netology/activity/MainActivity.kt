package ru.netology.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import ru.netology.activity.NewPostActivity.Companion.EDIT_POST
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.ActivityMainBinding

import ru.netology.dto.Post
import ru.netology.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    private val newPostRequestCode = 1
    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                val intent = Intent(this@MainActivity, NewPostActivity::class.java)
                    .putExtra(Intent.EXTRA_TEXT, post.content)
                    .setAction(EDIT_POST)
                startActivityForResult(intent, newPostRequestCode)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

        binding.rvPostsView.adapter = adapter
        viewModel.postsList.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.addButton.setOnClickListener {
            val intent = Intent(this, NewPostActivity::class.java)
            startActivityForResult(intent, newPostRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            newPostRequestCode -> {
                if (resultCode != Activity.RESULT_OK) {
                    return
                }

                data?.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    viewModel.changeContent(it)
                    viewModel.save()
                }
            }
        }
    }

}