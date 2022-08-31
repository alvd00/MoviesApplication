package com.alvd.movies.utils

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.movies.movies.utils.DASH_DATE_FORMAT
import com.example.movies.movies.utils.SPACE_DATE_FORMAT
import com.example.movies.movies.utils.TIME_STRING
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun View.showSnakeBar(text: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, length).show()
}

fun View.click(click: () -> Unit) = setOnClickListener { click() }

fun progressColor(value: Int): Int =
    try {
        when (value) {
            in 0..25 -> Color.parseColor("#FF0000")
            in 26..50 -> Color.parseColor("#FF8C00")
            in 51..75 -> Color.parseColor("#FFFF00")
            else -> Color.parseColor("#00FF00")
        }
    } catch (err: Exception) {
        Log.d("VVVV", "${err.localizedMessage}")
        0
    }

fun toDateString(value: String): String? =
    try {
        SimpleDateFormat(DASH_DATE_FORMAT, Locale.getDefault())
            .parse(value)?.let { date ->
                SimpleDateFormat(SPACE_DATE_FORMAT, Locale.getDefault()).format(date)
            }
    } catch (err: Exception) {
        null
    }

fun durationToString(duration: Int): String {
    val hours: Int = duration / 60
    val minutes: Int = duration % 60
    return String.format(TIME_STRING, hours, minutes)
}

fun releaseDateToString(date: String): String? {
    return try {
        var result: String = ""
        toDateString(date)?.let { date ->
            date.split(" ").forEach {
                result += it.replaceFirstChar(Char::titlecase) + " "
            }
        }
        result
    } catch (err: Exception) {
        null
    }
}

