package com.uwugram.domain.models

data class Message(
    var type: String = "",
    var text: String = "",
    var sender: String = "",
    var timestamp: Long = 0
)