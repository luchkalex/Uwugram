package com.uwugram.utils

import android.app.Activity
import android.provider.ContactsContract
import com.uwugram.domain.models.User

fun getUserContactsList(activity: Activity, userPhone: String): ArrayList<User> {
    val arrayContacts = arrayListOf<User>()
    val cursor = activity.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null,
    )
    while (cursor?.moveToNext() == true) {
        val displayNameColumnIndex =
            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val phoneNumberColumnIndex =
            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        val fullName = cursor.getString(displayNameColumnIndex)
        var phone = cursor.getString(phoneNumberColumnIndex)

        val newUser = User().apply {
            this.fullName = fullName
            if (!phone.startsWith("+")) {
                phone = "+38$phone"
            }
            this.phone = phone.replace(Regex("[\\s,-]"), "")
        }

        if (newUser.phone != userPhone)
            arrayContacts.add(newUser)
    }
    cursor?.close()
    return arrayContacts
}