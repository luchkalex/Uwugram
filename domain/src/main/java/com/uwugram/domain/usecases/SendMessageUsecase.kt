package com.uwugram.domain.usecases

import com.uwugram.domain.models.MessageTypes
import com.uwugram.domain.repositories.UserRepository

class SendMessageUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(
        message: String,
        contactId: String,
        messageType: MessageTypes,
        onSuccess: () -> Unit
    ) {
        return userRepository.sendMessage(message, contactId, messageType, onSuccess)
    }
}