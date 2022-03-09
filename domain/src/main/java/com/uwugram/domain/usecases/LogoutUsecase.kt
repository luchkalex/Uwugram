package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class LogoutUsecase(
    private val userRepository: UserRepository,
) {
    fun execute() {
        return userRepository.logout()
    }
}