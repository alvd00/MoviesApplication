package com.alvd.movies.ui.movies.adapter

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alvd.movies.domain.models.Movies
import com.alvd.movies.utils.MOVIES_PATH
import com.alvd.movies.utils.click
import com.alvd.movies.utils.progressColor
import com.alvd.movies.utils.releaseDateToString
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.movies.R
import com.example.movies.databinding.MovieItemBinding
import kotlin.math.roundToInt


class MovieViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val viewBinding: MovieItemBinding by viewBinding()

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(
        movie: Movies.Movie,
        delegate: MovieAdapter.Delegate?,
        position: Int,
        countItems: Int
    ) {
        with(viewBinding) {
            title.text = movie.title
            val popular = (movie.voteAverage * MULTIPLIER).roundToInt()
            rating.setProgress(popular, true)
            ratingValue.text = popular.toString()
            rating.setIndicatorColor(progressColor(popular))
            if (movie.releaseDate.isNullOrEmpty().not()) {
                releaseData.text = releaseDateToString(movie.releaseDate)
            }
            Glide.with(poster)
                .load(MOVIES_PATH.plus(movie.posterPath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(IMAGE_RADIUS)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions().crossFade(DELAY))
                .placeholder(R.drawable.ic_no_picture)
                .error(R.drawable.ic_no_picture)
                .into(poster)
            viewBinding.root.click { delegate?.onItemClick(movie) }
            if (countItems > ZERO_VALUE && position == countItems - FIVE_VALUE) {
                delegate?.getMoreMovies()
            }
        }
    }

    companion object {
        private const val MULTIPLIER = 10
        private const val IMAGE_RADIUS = 18
        private const val ZERO_VALUE = 0
        private const val FIVE_VALUE = 5
        private const val DELAY = 800
    }
}