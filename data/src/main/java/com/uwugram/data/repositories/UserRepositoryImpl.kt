package com.uwugram.data.repositories

import com.uwugram.data.UserStorage
import com.uwugram.domain.models.Message
import com.uwugram.domain.models.MessageTypes
import com.uwugram.domain.models.States
import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class UserRepositoryImpl(private val userStorage: UserStorage) :
    UserRepository {
    override fun updateUserState(state: States) {
        return userStorage.updateUserState(state.value)
    }

    override fun updateContacts(userContacts: ArrayList<User>) {
        return userStorage.updateUserContacts(userContacts)
    }

    override fun getUser(onValueUpdated: (User) -> Unit) {
        return userStorage.getUser(onValueUpdated)
    }

    override fun initializeUser(onComplete: (User) -> Unit) {
        return userStorage.initializeUser(onComplete)
    }

    override fun getChats(
        onChatFound: (User) -> Unit,
        onNoItemsFound: () -> Unit,
        onComplete: () -> Unit
    ) {
        return userStorage.getChats(onChatFound, onNoItemsFound, onComplete)
    }

    override fun checkAuthorized(onComplete: (Boolean) -> Unit) {
        return userStorage.checkAuthorized(onComplete)
    }

    override fun setUserImage(uri: String) {
        return userStorage.setUserImage(uri)
    }

    override fun logout() {
        return userStorage.logout()
    }

    override fun sendVerificationCode(
        phoneNumber: String,
        activity: Any,
        onCodeSend: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        return userStorage.sendVerificationCode(phoneNumber, activity, onCodeSend, onFailed)
    }

    override fun verifyCode(
        id: String,
        verificationCode: String,
        onLoggedIn: () -> Unit,
        onSigningUp: () -> Unit,
        onWrongCode: () -> Unit
    ) {
        return userStorage.verifyCode(id, verificationCode, onLoggedIn, onSigningUp, onWrongCode)
    }

    override fun getContacts(
        onNoItemsFound: () -> Unit,
        onItemFound: (User) -> Unit,
        onComplete: () -> Unit
    ) {
        return userStorage.getContacts(
            onNoItemsFound,
            onItemFound,
            onComplete
        )
    }

    override fun getContactInfo(userId: String, onComplete: (User) -> Unit) {
        return userStorage.getContactInfo(userId, onComplete)
    }

    override fun getMessages(
        userId: String,
        onNoItemsFound: () -> Unit,
        onItemFound: (Message) -> Unit,
        onComplete: (Int) -> Unit
    ) {
        return userStorage.getMessages(userId, onNoItemsFound, onItemFound, onComplete)
    }

    override fun sendMessage(
        message: String,
        contactId: String,
        messageType: MessageTypes,
        onSuccess: () -> Unit
    ) {
        return userStorage.sendMessage(message, contactId, messageType, onSuccess)
    }

    override fun getFullName(onComplete: (String) -> Unit) {
        return userStorage.getFullName(onComplete)
    }

    override fun updateFullName(fullName: String, onSuccess: () -> Unit) {
        return userStorage.updateFullName(fullName, onSuccess)
    }

    override fun signUp(fullName: String, onComplete: () -> Unit) {
        return userStorage.signUp(fullName, onComplete)
    }

    override fun getUsername(onComplete: (String) -> Unit) {
        return userStorage.getUsername(onComplete)
    }

    override fun updateUsername(
        username: String,
        onSuccess: () -> Unit,
        onUsernameAlreadyTaken: () -> Unit
    ) {
        return userStorage.updateUsername(username, onSuccess, onUsernameAlreadyTaken)
    }

    override fun updateBio(bio: String, onSuccess: () -> Unit) {
        return userStorage.updateBio(bio, onSuccess)
    }

    override fun getBio(onComplete: (String) -> Unit) {
        return userStorage.getBio(onComplete)
    }


}