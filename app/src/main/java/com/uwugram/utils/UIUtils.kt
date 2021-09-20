package com.uwugram.utils

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showShortToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(containerViewId, fragment).commit()
}

fun Fragment.replaceFragment(containerViewId: Int, fragment: Fragment) {
    activity?.supportFragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(containerViewId, fragment)?.commit()
}

