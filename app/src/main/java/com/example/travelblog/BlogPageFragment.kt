package com.example.travelblog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travelblog.databinding.ActivityBlogDetailsBinding

class BlogPageFragment: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageMain.setImageResource(R.drawable.sydney_image)
        binding.avatarImage.setImageResource(R.drawable.avatar)
        binding.arrowBack.setImageResource(R.drawable.arrow_back_white_24dp)
        binding.titleText.text = "Ехаем в Австралию"
        binding.authorName.text = "Игорешка"
        binding.postDate.text = "28 June 2023"
        binding.ratingValue.text = "4.4"
        binding.textViews.text = "1337"
        binding.textBody.text = "Не надо ехать в Австралию пожалуйста"
        binding.ratingBar.rating = 4.4f
        binding.arrowBack.setOnClickListener { finish() }
    }
}