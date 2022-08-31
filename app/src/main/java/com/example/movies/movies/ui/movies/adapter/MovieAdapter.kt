package com.alvd.movies.ui.movies.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alvd.movies.domain.models.Movies
import com.example.movies.R

/**
 * @author Borisov Andrey on 28.06.2022
 **/
class MovieAdapter(private val delegate: Delegate?) :
    RecyclerView.Adapter<MovieViewHolder?>() {

    interface Delegate {
        /**
         * Событие наступает при выборе
         * фильма из списка.
         * @param movie Фильм
         */
        fun onItemClick(movie: Movies.Movie)

        /**
         * Получить следующую порцию фильмов
         */
        fun getMoreMovies()
    }

    private val data = ArrayList<Movies.Movie>()

    fun setItems(newList: List<Movies.Movie>) {
        val result = DiffUtil.calculateDiff(
            DiffUtilCallback(
                data,
                newList as ArrayList<Movies.Movie>
            )
        )
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newList)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item,
                parent,
                false
            )
        )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(data[position], delegate, position, data.size)

    inner class DiffUtilCallback(
        private var oldItems: ArrayList<Movies.Movie>,
        private var newItems: ArrayList<Movies.Movie>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}