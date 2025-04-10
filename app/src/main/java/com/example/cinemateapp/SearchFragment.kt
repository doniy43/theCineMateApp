package com.example.cinemateapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemateapp.databinding.FragmentSearchBinding
import com.example.cinemateapp.models.SearchResult

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: SearchResultAdapter

    private val omdbApiKey = "3a8ce35e" // ✅ Your working OMDb API key

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        adapter = SearchResultAdapter(emptyList()) { selectedMovie ->
            // ✅ Navigate to MovieDetailActivity on click
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("title", selectedMovie.Title)
                putExtra("year", selectedMovie.Year)
                putExtra("poster", selectedMovie.Poster)
            }
            startActivity(intent)
        }

        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchResults.adapter = adapter

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.length >= 3) {
                    movieViewModel.searchMovies(omdbApiKey, query)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        movieViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.updateResults(results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
