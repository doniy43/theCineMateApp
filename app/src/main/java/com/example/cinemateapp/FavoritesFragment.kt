package com.example.cinemateapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemateapp.databinding.FragmentFavoritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteMovieAdapter
    private val movieList = mutableListOf<FavoriteMovie>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteMovieAdapter(movieList) { selectedMovie ->
            val intent = Intent(requireContext(), FavoriteDetailsActivity::class.java).apply {
                putExtra("movieId", selectedMovie.id)
                putExtra("title", selectedMovie.title)
                putExtra("year", selectedMovie.year)
                putExtra("studio", selectedMovie.studio)
                putExtra("rating", selectedMovie.rating)
                putExtra("posterUrl", selectedMovie.posterUrl)
            }
            startActivity(intent)
        }

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), FavoriteDetailsActivity::class.java)
            startActivity(intent)
        }

        loadFavoriteMovies()
    }


    override fun onResume() {
        super.onResume()
        loadFavoriteMovies()
    }

    private fun loadFavoriteMovies() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("favorites")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                movieList.clear()
                for (doc in documents) {
                    val movie = doc.toObject(FavoriteMovie::class.java).copy(id = doc.id)
                    movieList.add(movie)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
