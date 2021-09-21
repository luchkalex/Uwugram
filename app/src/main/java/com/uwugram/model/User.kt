package com.uwugram.model

data class User(
    val id: String = "",
    val phone: String = "",
    var username: String = "",
    var fullName: String = "",
    var bio: String = "",
    var status: String = "",
    var photoURL: String = "",
)
