package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class UpdateFullNameUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(fullName: String, onSuccess: () -> Unit) {
        return userRepository.updateFullName(fullName, onSuccess)
    }
}