package com.michaeludjiawan.moviedb.util

import android.view.View
import com.michaeludjiawan.moviedb.BuildConfig

fun toVisibility(constraint: Boolean): Int = if (constraint) {
    View.VISIBLE
} else {
    View.GONE
}

fun String.toImageUrl(): String {
    return StringBuilder().apply {
        append(BuildConfig.BASE_URL_IMAGE)
        append("original/")
        append(this@toImageUrl)
    }.toString()
}