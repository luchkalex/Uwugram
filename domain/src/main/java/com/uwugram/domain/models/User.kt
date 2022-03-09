package com.uwugram.domain.models

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
}
