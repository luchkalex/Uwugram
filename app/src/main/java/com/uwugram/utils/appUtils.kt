package com.uwugram.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun String.log() {
    println("\nLOG: $this\n")
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}