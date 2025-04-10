package com.example.cinemateapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemateapp.databinding.ItemSearchResultBinding
import com.example.cinemateapp.models.SearchResult

class SearchResultAdapter(
    private var results: List<SearchResult>,
    private val onItemClick: (SearchResult) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    inner class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: SearchResult) {
            binding.textViewTitle.text = result.Title
            binding.textViewStudio.text = result.Year
            Glide.with(binding.imageViewPoster.context)
                .load(result.Poster)
                .into(binding.imageViewPoster)

            binding.root.setOnClickListener {
                onItemClick(result)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    fun updateResults(newResults: List<SearchResult>) {
        results = newResults
        notifyDataSetChanged()
    }
}
