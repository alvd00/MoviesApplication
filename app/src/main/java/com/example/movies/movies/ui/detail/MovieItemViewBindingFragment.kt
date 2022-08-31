package com.alvd.movies.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.movies.movies.model.app_state.AppState
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.domain.models.MovieItem
import com.alvd.movies.ui.base.BaseViewBindingFragment
import com.alvd.movies.ui.detail.adapter.ActorAdapter
import com.alvd.movies.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.movies.R
import com.example.movies.databinding.FragmentDetailBinding
import com.example.movies.movies.utils.CORNER_RADIUS
import com.example.movies.movies.utils.ID_MOVIE
import com.example.movies.movies.utils.MULTIPLY_TEN
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class MovieItemViewBindingFragment : BaseViewBindingFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate),
    ActorAdapter.Delegate {

    private val viewModel: MovieItemViewModel by viewModel()
    private val movieId: Int? by lazy { arguments?.getInt(ID_MOVIE) }
    private val adapter by lazy { ActorAdapter(this) }

    override fun initListeners() {}
    override fun initObservers() {
        viewModel.getMovieLiveData()
            .observe(viewLifecycleOwner) { res -> renderData(result = res) }

        viewModel.getActorLiveData()
            .observe(viewLifecycleOwner) { res -> renderData(result = res) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerSetting()
        movieId?.let {
            viewModel.getMovieDetail(it)
            viewModel.getActors(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun renderSuccess(result: AppState.Success<*>) {
        showLoading(false)
        when (val result = result.data) {
            is MovieItem -> renderMovie(result)
            is Actors -> adapter.setItems(result.cast)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun renderMovie(movieItem: MovieItem) {
        with(viewBinding) {
            title.text = movieItem.title
            Glide.with(miniImage)
                .load(MOVIES_PATH.plus(movieItem.backdropPath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(CORNER_RADIUS)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions().crossFade(DELAY))
                .error(R.drawable.ic_no_picture)
                .into(miniImage)
            Glide.with(poster)
                .load(MOVIES_PATH.plus(movieItem.posterPath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(CORNER_RADIUS)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions().crossFade(DELAY))
                .error(R.drawable.ic_no_picture)
                .into(poster)
            val popular = (movieItem.voteAverage * MULTIPLY_TEN).roundToInt()
            rating.setProgress(popular, true)
            ratingValue.text = popular.toString()
            rating.setIndicatorColor(progressColor(popular))

            movieItem.releaseDate?.let { it ->
                toDateString(it)?.let {
                    if (it.isNotEmpty()) {
                        releaseData.text = it
                    }
                }
            }

            movieItem.runtime?.let {
                duration.text = durationToString(it)
            }
            genre.text =
                movieItem.genres.joinToString(separator = ", ") { it ->
                    it.name.replaceFirstChar(
                        Char::titlecase
                    )
                }
            movieItem.overview?.let {
                description.text = it
                descriptionTitle.isVisible = it.isNotEmpty()
            }
        }
    }

    private fun initRecyclerSetting() {
        viewBinding.recyclerView.also { recycler ->
            recycler.adapter = adapter
            recycler.setHasFixedSize(true)
        }
    }

    override fun showLoading(isShow: Boolean) {
//        viewBinding.progress.isVisible = isShow
    }

    override fun onItemClick(actor: Actors.Cast) {}

    override fun showError(throwable: Throwable) {
        viewBinding.root.showSnakeBar(throwable.localizedMessage)
    }

    companion object {
        const val DELAY = 800
    }
}