package ru.netology.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.R
import ru.netology.databinding.ActivityNewPostBinding


class NewPostActivity : AppCompatActivity() {

    companion object {
        const val EDIT_POST = "EDIT_POST"
        const val VIDEO_URL = "VIDEO_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {

            if (it.action != EDIT_POST) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            val videoUrl = it.getStringExtra(VIDEO_URL)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }
                    .show()
                return@let
            }
            binding.editView.setText(text)
            binding.videoUrlView.setText(videoUrl ?: "")
        }

        binding.okButton.setOnClickListener {
            val text = binding.editView.text?.toString()
            val videoUrl = binding.videoUrlView.text?.toString()

            if (text.isNullOrBlank()) {
                Toast.makeText(applicationContext, getString(R.string.error_empty_content), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                    .putExtra(Intent.EXTRA_TEXT, text)
                    .putExtra(VIDEO_URL, videoUrl)
                setResult(Activity.RESULT_OK, intent)

                finish()
            }
        }
    }
}