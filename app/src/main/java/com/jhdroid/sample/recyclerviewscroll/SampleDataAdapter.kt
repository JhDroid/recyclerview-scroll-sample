package com.jhdroid.sample.recyclerviewscroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jhdroid.sample.recyclerviewscroll.databinding.ItemSampleDataBinding

class SampleDataAdapter : ListAdapter<String, SampleDataAdapter.ViewHolder>(SampleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: ItemSampleDataBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.data = data
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                return ViewHolder(ItemSampleDataBinding.inflate(layoutInflater))
            }
        }
    }

    class SampleDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}