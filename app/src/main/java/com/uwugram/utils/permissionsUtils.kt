package com.uwugram.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
fun checkPermission(permission: String, activity: Activity): Boolean {
    return !(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
        activity, permission
    ) != PackageManager.PERMISSION_GRANTED)
}