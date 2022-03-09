package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class SignUpUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(fullName: String, onComplete: () -> Unit) {
        return userRepository.signUp(fullName, onComplete)
    }
}