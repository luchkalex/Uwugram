package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class UpdateUsernameUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(username: String, onSuccess: () -> Unit, onUsernameAlreadyTaken: () -> Unit) {
        return userRepository.updateUsername(username, onSuccess, onUsernameAlreadyTaken)
    }
}