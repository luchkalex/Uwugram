package com.uwugram.domain.usecases

import com.uwugram.domain.models.Message
import com.uwugram.domain.repositories.UserRepository

class GetMessagesUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(
        userId: String,
        onNoItemsFound: () -> Unit,
        onItemFound: (Message) -> Unit,
        onComplete: (Int) -> Unit
    ) {
        return userRepository.getMessages(userId, onNoItemsFound, onItemFound, onComplete)
    }
}