package com.uwugram.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun String.log() {
    System.err.println("\nLOG: $this\n")
}