package com.example.cinemateapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemateapp.databinding.ItemFavoriteMovieBinding

class FavoriteMovieAdapter(
    private val movies: List<FavoriteMovie>,
    private val onItemClick: (FavoriteMovie) -> Unit
) : RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemFavoriteMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.textTitleFav.text = movie.title
        holder.binding.textYearFav.text = movie.year
        holder.binding.textRatingFav.text = "Rating: ${movie.rating}"
        holder.binding.textStudioFav.text = "Studio: ${movie.studio}"

        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .into(holder.binding.imagePosterFav)

        holder.itemView.setOnClickListener {
            onItemClick(movie)
        }
    }
}
