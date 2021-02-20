package ru.netology.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.util.AndroidUtils
import ru.netology.util.PairStringDelegate
import ru.netology.viewmodel.PostViewModel


class NewPostFragment : Fragment() {

    companion object {
        var Bundle.isData: Pair<String, String?>? by PairStringDelegate
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        arguments?.isData?.let {
            binding.editView.setText(it.first)
            binding.videoUrlView.setText(it.second)
        }

        binding.okButton.setOnClickListener {
            val text = binding.editView.text?.toString()
            val videoUrl = binding.videoUrlView.text?.toString()

            if (!text.isNullOrBlank()) {
                viewModel.changeContent(text, videoUrl)
                viewModel.save()
            }
            AndroidUtils.hideKeyboard(binding.root)
            findNavController().navigateUp()
        }

        return binding.root
    }
}