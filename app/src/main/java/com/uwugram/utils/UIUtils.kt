package com.uwugram.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager).hideSoftInputFromWindow(
        activity.window.decorView.windowToken,
        0
    )
}