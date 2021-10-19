package com.uwugram.model

import com.uwugram.utils.States

data class User(
    var id: String = "",
    var phone: String = "",
    var username: String = "",
    var fullName: String = "",
    var bio: String = "",
    var photoURL: String = "empty",
    var message: Message = Message(),
    private var _state: States = States.NONE
) {

    var state
        get() = States.getStringFromState(_state)
        set(value) {
            _state = States.getStateFromString(value)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
