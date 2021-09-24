package com.uwugram.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.uwugram.R

fun Fragment.showShortToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(
    containerViewId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    val fragmentTransaction = supportFragmentManager.beginTransaction()
        .replace(containerViewId, fragment)

    if (addToBackStack)
        fragmentTransaction.addToBackStack(null)

    fragmentTransaction.commit()
}

fun Fragment.replaceFragment(containerViewId: Int, fragment: Fragment) {
    activity?.supportFragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(containerViewId, fragment)?.commit()
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

