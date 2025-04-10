package com.example.cinemateapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cinemateapp.databinding.ActivityMovieDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var movieId: String? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: ""
        val year = intent.getStringExtra("year") ?: ""
        val poster = intent.getStringExtra("poster") ?: ""

        binding.textViewDetailTitle.text = title
        binding.textViewDetailYear.text = "Year: $year"
        Glide.with(this).load(poster).into(binding.imageViewDetailPoster)

        checkIfFavorite(title, year, poster)

        binding.buttonAddToFavorites.setOnClickListener {
            if (isFavorite) {
                removeFromFavorites()
            } else {
                addToFavorites(title, year, poster)
            }
        }

        binding.buttonGoBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun checkIfFavorite(title: String, year: String, poster: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("favorites")
            .whereEqualTo("userId", userId)
            .whereEqualTo("title", title)
            .whereEqualTo("year", year)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    isFavorite = true
                    movieId = documents.first().id
                    binding.buttonAddToFavorites.text = "Remove from Favorites"
                } else {
                    isFavorite = false
                    binding.buttonAddToFavorites.text = "Add to Favorites"
                }
            }
    }

    private fun addToFavorites(title: String, year: String, poster: String) {
        val userId = auth.currentUser?.uid ?: return
        val data = hashMapOf(
            "userId" to userId,
            "title" to title,
            "year" to year,
            "studio" to "",  // Optional: Add if needed
            "rating" to "",  // Optional
            "posterUrl" to poster
        )
        db.collection("favorites")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                binding.buttonAddToFavorites.text = "Remove from Favorites"
                isFavorite = true
                movieId = it.id
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromFavorites() {
        val id = movieId ?: return
        db.collection("favorites")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                binding.buttonAddToFavorites.text = "Add to Favorites"
                isFavorite = false
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to remove", Toast.LENGTH_SHORT).show()
            }
    }
}
