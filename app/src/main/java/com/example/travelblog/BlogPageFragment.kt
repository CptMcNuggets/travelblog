package com.example.travelblog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.travelblog.databinding.ActivityBlogDetailsBinding
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import com.google.android.material.snackbar.Snackbar

class BlogPageFragment: AppCompatActivity() {
    private lateinit var binding: ActivityBlogDetailsBinding
    companion object { //used to declare constants in the class or functions which don’t require instances of the object.
        private const val EXTRAS_BLOG = "EXTRAS_BLOG"

        fun start(activity: Activity, blog: Blog) {
            val intent = Intent(activity, BlogPageFragment::class.java)
            intent.putExtra(EXTRAS_BLOG, blog)
            activity.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.arrowBack.setImageResource(R.drawable.arrow_back_white_24dp)
        binding.arrowBack.setOnClickListener { finish() }
        intent.extras?.getParcelable<Blog>(EXTRAS_BLOG)?.let { blog: Blog ->
            showData(blog = blog)
        }

    }
    private fun showData(blog: Blog) {
        binding.titleText.text = blog.title
        binding.postDate.text = blog.date
        binding.authorName.text = blog.author.name
        binding.ratingValue.text = blog.rating.toString()
        binding.textViews.text = String.format("(%d views)", blog.views)
        binding.textBody.text = Html.fromHtml(blog.description, FROM_HTML_MODE_COMPACT)
        binding.ratingBar.rating = blog.rating
        binding.progressBar.visibility = View.GONE
        binding.ratingBar.visibility = View.VISIBLE

        Glide.with(this)
            .load(blog.getImageUrl())
            .into(binding.imageMain)
        Glide.with(this)
            .load(blog.author.getAvatarUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.avatarImage)
    }
}