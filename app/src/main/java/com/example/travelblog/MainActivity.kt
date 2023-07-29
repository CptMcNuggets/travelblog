package com.example.travelblog

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelblog.adapter.MainAdapter
import com.example.travelblog.databinding.ActivityMainBinding
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import com.example.travelblog.repository.BlogRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SORT_TITLE = 0
        private const val SORT_DATE = 1
    }

    private val repository by lazy { BlogRepository(context = applicationContext) }
    private lateinit var binding: ActivityMainBinding
    private val adapter = MainAdapter {blog ->
        BlogPageFragment.start(this, blog = blog)
    }
    private var currentSort = SORT_DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchItem = binding.toolbar.menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filter(newText)
                }
                return true
            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.refresh.setOnRefreshListener {
            loadDataFromNetwork()
        }
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.sort) {
                onSortClicked()
            }
            false
        }
        loadDataFromNetwork()
        loadDataFromDatabase()
    }

    private fun loadDataFromNetwork() {
        binding.refresh.isRefreshing = true
        repository.loadDataFromNetwork(
            onSuccess = {blogList: List<Blog> ->
                binding.refresh.isRefreshing = false
                adapter.setData(blogList)
                sortData()
            },
            onError = {
                runOnUiThread {
                    binding.refresh.isRefreshing = false
                    showErrorSnackbar()
                }
            }
        )

    }
    private fun loadDataFromDatabase() {
        repository.loadFromDatabase { blogList: List<Blog> ->
            runOnUiThread {
                adapter.setData(blogList)
                sortData()
            }
        }
    }
    private fun onSortClicked() {
        val items = arrayOf("Title", "Date")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort order")
            .setSingleChoiceItems(items, currentSort) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                currentSort = which
                sortData()
            }.show()
    }
    private fun sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle()
        }
        if (currentSort == SORT_DATE) {
            adapter.sortByDate()
        }
    }
    private fun showErrorSnackbar() {
        Snackbar.make(binding.root,
            "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE).run {
            setActionTextColor(resources.getColor(R.color.orange500, context.theme))
            setAction("Retry") {
                loadDataFromNetwork()
                dismiss()
            }
        }.show()
    }
}