package com.uwugram.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.uwugram.R

fun Fragment.showShortToast(message: String) {
    context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
}

fun AppCompatActivity.showShortToast(message: String) {
    let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
}


fun hideKeyboard(activity: Activity) {
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager).hideSoftInputFromWindow(
        activity.window.decorView.windowToken,
        0
    )
}

fun ImageView.downloadAndSetImage(photoURL: String) {
    Picasso.get()
        .load(photoURL)
        .placeholder(R.drawable.default_avatar)
        .into(this)
}

fun View.fadeIn() {
    startAnimation(AnimationUtils.loadAnimation(MAIN_ACTIVITY, R.anim.fade_in))
    visibility = View.VISIBLE
}

fun View.fadeOut() {
    startAnimation(AnimationUtils.loadAnimation(MAIN_ACTIVITY, R.anim.fade_out))
    visibility = View.GONE
}

fun setMargins(
    v: View,
    l: Int = v.marginLeft,
    t: Int = v.marginTop,
    r: Int = v.marginRight,
    b: Int = v.marginBottom
) {
    if (v.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = v.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(l, t, r, b)
        v.requestLayout()
    }
}


