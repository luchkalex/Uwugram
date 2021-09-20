package com.uwugram.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT:DatabaseReference

const val NODE_USERS = "users"
const val FIELD_USERS_ID = "id"
const val FIELD_USERS_PHONE = "phone"
const val FIELD_USERS_USERNAME = "username"


fun initFirebase(){
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
}