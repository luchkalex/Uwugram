package com.uwugram.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS

enum class PermissionCodes(val code: Int) {
    PERMISSION_READ_CONTACTS_REQUEST_CODE(200)
}


fun checkPermission(permission: String): Boolean {
    return !(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
        MAIN_ACTIVITY,
        permission
    ) != PackageManager.PERMISSION_GRANTED)
}