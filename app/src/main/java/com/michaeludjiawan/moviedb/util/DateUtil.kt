package com.michaeludjiawan.moviedb.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)

    fun format(date: Date): String = dateFormat.format(date)

}