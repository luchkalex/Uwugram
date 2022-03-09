package com.uwugram.data

import com.uwugram.domain.models.Message
import com.uwugram.domain.models.MessageTypes
import com.uwugram.domain.models.User

interface UserStorage {
    fun updateUserState(state: String)
    fun updateUserContacts(userContacts: ArrayList<User>)
    fun getUser(onValueUpdated: (User) -> Unit)
    fun initializeUser(onComplete: (User) -> Unit)
    fun getChats(onChatFound: (User) -> Unit, onNoItemsFound: () -> Unit, onComplete: () -> Unit)
    fun checkAuthorized(onComplete: (Boolean) -> Unit)
    fun setUserImage(uri: String)
    fun logout()
    fun sendVerificationCode(
        phoneNumber: String,
        activity: Any,
        onCodeSend: (String) -> Unit,
        onFailed: () -> Unit
    )

    fun verifyCode(
        id: String,
        verificationCode: String,
        onLoggedIn: () -> Unit,
        onSigningUp: () -> Unit,
        onWrongCode: () -> Unit
    )

    fun getContacts(
        onNoItemsFound: () -> Unit,
        onItemFound: (User) -> Unit,
        onComplete: () -> Unit
    )

    fun getContactInfo(userId: String, onComplete: (User) -> Unit)

    fun getMessages(
        userId: String,
        onNoItemsFound: () -> Unit,
        onItemFound: (Message) -> Unit,
        onComplete: (Int) -> Unit
    )

    fun sendMessage(
        message: String,
        contactId: String,
        messageType: MessageTypes,
        onSuccess: () -> Unit
    )

    fun getFullName(onComplete: (String) -> Unit)

    fun updateFullName(fullName: String, onSuccess: () -> Unit)

    fun signUp(fullName: String, onComplete: () -> Unit)

    fun getUsername(onComplete: (String) -> Unit)

    fun updateUsername(username: String, onSuccess: () -> Unit, onUsernameAlreadyTaken: () -> Unit)

    fun updateBio(bio: String, onSuccess: () -> Unit)

    fun getBio(onComplete: (String) -> Unit)
}