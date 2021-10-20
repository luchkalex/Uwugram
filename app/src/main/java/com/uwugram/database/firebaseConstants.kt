package com.uwugram.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.uwugram.model.User

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var REF_NODE_USER_MESSAGES: DatabaseReference
lateinit var REF_NODE_USER_CONTACTS: DatabaseReference
lateinit var REF_NODE_USERS: DatabaseReference

lateinit var UID: String

var USER = User()

const val NODE_USERS = "users"
const val NODE_USERNAME = "username"
const val NODE_PHONE = "phone"
const val NODE_PHONE_CONTACTS = "phoneContacts"
const val NODE_MESSAGES = "messages"

const val FOLDER_PROFILE_IMAGES = "profileImages"

const val FIELD_USERS_ID = "id"
const val FIELD_USERS_PHONE = "phone"
const val FIELD_USERS_USERNAME = "username"
const val FIELD_USERS_FULLNAME = "fullName"
const val FIELD_USERS_BIO = "bio"
const val FIELD_USERS_STATE = "state"
const val FIELD_USERS_PHOTO_URL = "photoURL"

const val FIELD_SENDER = "sender"
const val FIELD_TEXT = "text"
const val FIELD_TYPE = "type"
const val FIELD_TIMESTAMP = "timestamp"