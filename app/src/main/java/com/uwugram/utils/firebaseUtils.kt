package com.uwugram.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uwugram.model.User

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User
lateinit var UID: String


const val NODE_USERS = "users"
const val NODE_USERNAME = "username"

const val FOLDER_PROFILE_IMAGES = "profileImages"

const val FIELD_USERS_ID = "id"
const val FIELD_USERS_PHONE = "phone"
const val FIELD_USERS_USERNAME = "username"
const val FIELD_USERS_FULLNAME = "fullName"
const val FIELD_USERS_BIO = "bio"
const val FIELD_USERS_STATUS = "status"
const val FIELD_USERS_PHOTO_URL = "photoURL"


fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun savePhotoUrlToDataBase(uri: Uri, crossinline onSuccess: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FIELD_USERS_PHOTO_URL)
        .setValue(uri.toString()).addOnSuccessListener { onSuccess() }
}

inline fun getImageUrl(storageRef: StorageReference, crossinline onSuccess: (Uri) -> Unit) {
    storageRef.downloadUrl.addOnSuccessListener { onSuccess(it) }
}

inline fun putImageToStorage(
    uri: Uri?,
    storageRef: StorageReference,
    crossinline onSuccess: () -> Unit
) {
    uri?.let {
        storageRef.putFile(it).addOnSuccessListener { onSuccess() }
    }
}