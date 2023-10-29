package com.example.libraryapp.ui.home.book

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libraryapp.R
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.databinding.ItemBookBinding

class RecyclerBookAdapter :
    ListAdapter<Book, RecyclerBookAdapter.ViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.book = book

            if (!book.bookImageUri.isNullOrBlank()) {
                Glide.with(itemView.context).load(book.bookImageUri)
                    .into(binding.bookImage)
            } else {
                binding.bookImage.setImageResource(R.drawable.ic_book)
            }
            binding.executePendingBindings()
        }
    }

    private class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.bookID == newItem.bookID
        }
    }
}