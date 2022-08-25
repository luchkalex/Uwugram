package com.uwugram.domain.models

enum class States(val value: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    NONE("none");


    companion object {
        fun getStringFromState(state: States): String {
            return when (state) {
                ONLINE -> "online"
                OFFLINE -> "offline"
                NONE -> "none"
            }
        }

        fun getStateFromString(string: String): States {
            return when (string) {
                ONLINE.value -> ONLINE
                OFFLINE.value -> OFFLINE
                else -> NONE
            }
        }
    }
}