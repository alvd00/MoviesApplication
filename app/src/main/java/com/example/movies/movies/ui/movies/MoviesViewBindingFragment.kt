package com.alvd.movies.ui.movies

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.NavHostFragment
import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.ui.base.BaseViewBindingFragment
import com.alvd.movies.ui.movies.adapter.MovieAdapter
import com.alvd.movies.utils.showSnakeBar
import com.example.movies.R
import com.example.movies.databinding.FragmentMoviesBinding
import com.example.movies.movies.utils.ID_MOVIE
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviesViewBindingFragment() : BaseViewBindingFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate),
    MovieAdapter.Delegate {

    private val viewModel: MoviesViewModel by viewModel()
    private val adapter by lazy { MovieAdapter(this) }

    override fun initListeners() {
        viewBinding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                if (view.text.isNotEmpty()) {
                    viewModel.setCurrentPage(1, 1)
                    viewModel.searchMovies(viewBinding.searchEditText.text.toString())
                    hideKeyboardForTextView()
                    true
                } else {
                    hideKeyboardForTextView()
                    false
                }
            } else {
                false
            }
        }

        viewBinding.search.setEndIconOnClickListener {
            viewModel.setCurrentPage(1, 1)
            viewModel.getMoviesTop(true)
            viewBinding.searchEditText.setText("")
            hideKeyboardForTextView()
        }

        viewBinding.searchEditText.doAfterTextChanged {
            it?.let {
                if (it.length > MIN_VALUE_TO_SEARCH) {
                    viewModel.searchMovies(viewBinding.searchEditText.text.toString())
                } else {
                    viewModel.getMoviesTop(true)
                }
            }
        }
    }

    private fun hideKeyboardForTextView() {
        val view = requireActivity().currentFocus
        view?.let {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                        InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, INPUT_METHOD_MANAGER_FLAGS)
        }
        (view as? TextView)?.clearFocus()
    }

    override fun initObservers() {
        viewModel.getMoviesLiveData()
            .observe(viewLifecycleOwner) { res -> renderData(result = res) }
    }

    override fun renderSuccess(result: AppState.Success<*>) {
        showLoading(false)
        when (val moveResponse = result.data) {
            is Movies -> {
                renderMovies(moveResponse)
            }
        }
    }

    private fun renderMovies(movies: Movies) {
        adapter.setItems(movies.movies)
        if (adapter.itemCount > 0)
            viewModel.setCurrentPage(movies.page, movies.totalPages)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerSetting()
    }

    private fun initRecyclerSetting() {
        viewBinding.recyclerView.also { recycler ->
            recycler.adapter = adapter
            recycler.setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewBinding.searchEditText.text.toString().isEmpty()) {
            viewModel.getMoviesTop(true)
        } else {
            viewModel.setCurrentPage(1, 1)
        }
    }



    override fun onItemClick(movie: Movies.Movie) {
        NavHostFragment.findNavController(this).navigate(R.id.nav_movie_item_fragment
            , bundleOf().apply {
            putInt(ID_MOVIE, movie.id)
        })
    }

    override fun getMoreMovies() {
        if (viewBinding.searchEditText.text.toString().isEmpty()) {
            viewModel.getMoviesTop(true)
        }
    }

    override fun showLoading(isShow: Boolean) {
    }

    override fun showError(throwable: Throwable) {
        viewBinding.root.showSnakeBar(throwable.localizedMessage)
    }

    companion object {
        private const val MIN_VALUE_TO_SEARCH = 2
        private const val INPUT_METHOD_MANAGER_FLAGS = 0
    }
}