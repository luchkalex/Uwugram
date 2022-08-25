package com.uwugram.domain.usecases

import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class GetChatsUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onItemFound: (User) -> Unit, onNoItemsFound: () -> Unit, onComplete: () -> Unit) {
        return userRepository.getChats(onItemFound, onNoItemsFound, onComplete)
    }
}