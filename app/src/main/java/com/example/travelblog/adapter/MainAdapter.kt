package com.example.travelblog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.travelblog.databinding.ActivityListItemBinding
import com.example.travelblog.http.Blog

class MainAdapter(private val onItemClickListener: (Blog) -> Unit) : ListAdapter<Blog, MainViewHolder>(DIFF_CALLBACK) {

    private var originalList : List<Blog> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityListItemBinding.inflate(inflater, parent, false)

        return MainViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
    fun sortByTitle() {
        submitList(currentList.sortedBy { blog -> blog.title })
    }
    fun sortByDate() {
        submitList(currentList.sortedByDescending { blog -> blog.getDateMillis() })
    }
    fun setData(list: List<Blog>) {
        originalList = list
        super.submitList(list)
    }
    fun filter(query: String) {
        submitList(originalList.filter { blog ->
            blog.title.contains(query, ignoreCase = true)
        })
    }
}

class MainViewHolder(private val binding: ActivityListItemBinding, private val onItemClickListener: (Blog) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(blog: Blog) {
        binding.textTitle.text = blog.title
        binding.textDate.text = blog.date
        binding.root.setOnClickListener { onItemClickListener(blog) }

        Glide.with(itemView)
            .load(blog.author.getAvatarUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAvatar)
    }
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Blog> = object : DiffUtil.ItemCallback<Blog>() {
    override fun areItemsTheSame(oldData: Blog,
                                 newData: Blog): Boolean {
        return oldData.id == newData.id
    }

    override fun areContentsTheSame(oldData: Blog,
                                    newData: Blog): Boolean {
        return oldData == newData
    }
}