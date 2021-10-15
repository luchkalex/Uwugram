package com.uwugram.model

data class Message(
    var type: String = "",
    var text: String = "",
    var sender: String = "",
    var timestamp: Any = ""
)