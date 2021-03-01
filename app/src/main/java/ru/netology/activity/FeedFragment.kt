package ru.netology.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.activity.NewPostFragment.Companion.isData
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostsAdapter
import ru.netology.databinding.FragmentFeedBinding
import ru.netology.dto.Post
import ru.netology.util.LongDelegate
import ru.netology.viewmodel.PostViewModel


class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    companion object {
        var Bundle.isPostId: Long? by LongDelegate
        var postId: Long? = null;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        postId = arguments?.isPostId

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { isData = post.content }
                )
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id, post.likedByMe)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                if (postId != null) findNavController().navigateUp()
            }

            /*override fun onVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_video))

                startActivity(shareIntent)
            }*/

            override fun onPost(post: Post) {
                if (postId == null)
                    findNavController().navigate(
                        R.id.action_feedFragment_self,
                        Bundle().apply { isPostId = post.id }
                    )
            }
        })


        binding.rvPostsView.adapter = adapter

        if (postId != null) {
            viewModel.postsList.observe(viewLifecycleOwner, { state ->
                val post = state.posts.find { it2 -> it2.id == postId }
                adapter.submitList(listOf(post))
                binding.progress.isVisible = state.loading
                binding.errorGroup.isVisible = state.error
                binding.emptyText.isVisible = state.empty
            })
        } else {
            viewModel.postsList.observe(viewLifecycleOwner, { state ->
                adapter.submitList(state.posts)
                binding.progress.isVisible = state.loading
                binding.errorGroup.isVisible = state.error
                binding.emptyText.isVisible = state.empty
            })
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}