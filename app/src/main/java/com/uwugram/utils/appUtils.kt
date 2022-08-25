package com.uwugram.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.models.User
import java.text.SimpleDateFormat
import java.util.*

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return timeFormat.format(time)
}

fun Activity.replaceActivity(activity: Activity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

@ExperimentalCoilApi
@ExperimentalAnimationApi
fun <T> MutableList<T>.addUniqueItemToList(item: T) {
    if (!this.contains(item)) {
        this.add(item)
    }
}

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
fun MutableList<User>.addUniqueUserToList(item: User) {
    this.forEach { currentContact ->
        if (currentContact.id == item.id) {
            this.remove(currentContact)
            this.add(item)
            return
        }
    }
    this.add(item)
}

fun Context.getActivity(): Activity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}