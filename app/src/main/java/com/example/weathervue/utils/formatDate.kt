package com.example.weathervue.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun formatDate(timestamp: Int): String {
    val sdf = SimpleDateFormat("EEE,\nMMM d")
    val date = java.util.Date(timestamp.toLong() * 1000)

    return sdf.format(date)
}

@SuppressLint("SimpleDateFormat")
fun formatDate2(timestamp: Int): String {
    val sdf = SimpleDateFormat("EEE, MMM d")
    val date = java.util.Date(timestamp.toLong() * 1000)

    return sdf.format(date)
}
