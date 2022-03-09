package com.uwugram.domain.usecases

import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class InitializeUserUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onComplete: (User) -> Unit) {
        return userRepository.initializeUser(onComplete)
    }
}