package com.michaeludjiawan.moviedb.util

import android.os.Build
import android.text.Html
import android.text.Spanned
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

val String.formatHtml: Spanned
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }