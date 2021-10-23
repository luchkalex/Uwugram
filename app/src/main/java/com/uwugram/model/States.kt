package com.uwugram.model

import com.uwugram.R
import com.uwugram.utils.MAIN_ACTIVITY

enum class States(val value: String) {
    ONLINE("online"),
    OFFLINE("offline"),
    NONE("none");

    companion object {
        fun getStringFromState(state: States): String {
            return when (state) {
                ONLINE -> MAIN_ACTIVITY.getString(R.string.online_status)
                OFFLINE -> MAIN_ACTIVITY.getString(R.string.offline_status)
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