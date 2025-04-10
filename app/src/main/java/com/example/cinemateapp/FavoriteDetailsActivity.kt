package com.example.cinemateapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemateapp.databinding.ActivityFavoriteDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteDetailsBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var movieId: String? = null
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if editing
        val extras = intent.extras
        if (extras != null) {
            isEditing = true
            movieId = extras.getString("movieId")
            binding.editTitle.setText(extras.getString("title"))
            binding.editYear.setText(extras.getString("year"))
            binding.editStudio.setText(extras.getString("studio"))
            binding.editRating.setText(extras.getString("rating"))
            binding.editPosterUrl.setText(extras.getString("posterUrl"))
            binding.buttonDelete.visibility = android.view.View.VISIBLE
        }

        binding.buttonSave.setOnClickListener {
            val title = binding.editTitle.text.toString().trim()
            val year = binding.editYear.text.toString().trim()
            val studio = binding.editStudio.text.toString().trim()
            val rating = binding.editRating.text.toString().trim()
            val posterUrl = binding.editPosterUrl.text.toString().trim()

            val movie = hashMapOf(
                "title" to title,
                "year" to year,
                "studio" to studio,
                "rating" to rating,
                "posterUrl" to posterUrl,
                "userId" to auth.currentUser?.uid
            )

            if (isEditing && movieId != null) {
                db.collection("favorites").document(movieId!!)
                    .set(movie)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } else {
                db.collection("favorites")
                    .add(movie)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
        }

        binding.buttonDelete.setOnClickListener {
            movieId?.let {
                db.collection("favorites").document(it)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
        }
    }
}
