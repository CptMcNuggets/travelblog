package com.example.travelblog.repository

import android.content.Context
import com.example.travelblog.database.DatabaseProvider
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import java.util.concurrent.Executors

class BlogRepository(context: Context) {
    private val httpClient = BlogHttpClient
    private val database = DatabaseProvider.getInstance(context.applicationContext)
    private val executor = Executors.newSingleThreadExecutor()

    fun loadFromDatabase(callback: (List<Blog>) -> Unit) {
        executor.execute {
            callback(database.blogDao().getAll())
        }
    }
    fun loadDataFromNetwork(onSuccess: (List<Blog>) -> Unit, onError: () -> Unit) {
        val blogList = BlogHttpClient.loadBlogArticles()
        if (blogList == null) {
            onError()
        } else {
            val blogDao = database.blogDao()
            blogDao.deleteAll()
            blogDao.insertAll(blogList)
            onSuccess(blogList)
        }
    }
}
