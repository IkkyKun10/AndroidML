package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.databinding.ItemListHistoryBinding
import com.dicoding.asclepius.model.ResultModel

class HistoryAdapter : ListAdapter<ResultModel, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultModel>() {
            override fun areItemsTheSame(oldItem: ResultModel, newItem: ResultModel): Boolean =
                oldItem.imageUri == newItem.imageUri


            override fun areContentsTheSame(oldItem: ResultModel, newItem: ResultModel): Boolean =
                oldItem == newItem

        }
    }

    class ViewHolder(private val binding: ItemListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResultModel) {
            with(binding) {
                imgHistory.setImageURI(item.imageUri)
                tvResult.text = buildString {
                    append(item.resultLabel)
                    append(" ")
                    append(item.resultScore)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}