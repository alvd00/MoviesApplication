package com.alvd.movies.ui.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.alvd.movies.domain.models.Actors
import com.alvd.movies.utils.MOVIES_PATH
import com.alvd.movies.utils.click
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.movies.R
import com.example.movies.databinding.ActorItemBinding
import com.example.movies.movies.utils.CORNER_RADIUS


class ActorViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val viewBinding: ActorItemBinding by viewBinding()

    fun bind(actor: Actors.Cast, delegate: ActorAdapter.Delegate?) {
        with(viewBinding) {
            name.text = actor.name
            character.text = actor.character
            Glide.with(poster)
                .load(MOVIES_PATH.plus(actor.profilePath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(CORNER_RADIUS)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions().crossFade(DELAY))
                .placeholder(R.drawable.ic_no_picture)
                .error(R.drawable.ic_no_picture)
                .into(poster)
            viewBinding.root.click { delegate?.onItemClick(actor) }
        }
    }

    companion object {
        private const val DELAY = 800
    }
}