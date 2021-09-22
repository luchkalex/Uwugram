package com.uwugram.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.uwugram.model.User

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: User
lateinit var UID: String


const val NODE_USERS = "users"
const val NODE_USERNAME = "username"
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
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}