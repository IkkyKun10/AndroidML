package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ItemListNewsBinding
import com.dicoding.asclepius.domain.News

class NewsAdapter(private val onClicked: (News) -> Unit) : ListAdapter<News, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
                override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                    oldItem.id == newItem.id


                override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                    oldItem == newItem

            }
        }

    inner class ViewHolder(private val binding: ItemListNewsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: News){
            with(binding){
                if (item.urlToImage == null) {
                    imgNews.setImageResource(R.drawable.ic_place_holder)
                } else {
                    imgNews.load(item.urlToImage) {
                        crossfade(true)
                        placeholder(R.drawable.ic_place_holder)
                        transformations(CircleCropTransformation())
                    }
                }

                tvSource.text = item.name
                tvTitle.text = item.title
                tvPublished.text = item.publishedAt

                itemView.setOnClickListener {
                    onClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}