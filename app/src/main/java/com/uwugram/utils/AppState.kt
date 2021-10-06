package com.uwugram.utils

enum class AppState(val state: String) {
    ONLINE("online"),
    OFFLINE("offline");

    companion object {
        fun updateState(appState: AppState) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FIELD_USERS_STATE)
                .setValue(appState.state)
                .addOnSuccessListener { USER.status = appState.state }
        }
    }
}

enum class Signals {
    START,
    STOP,
    REPLACE;
}