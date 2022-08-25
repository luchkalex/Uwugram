package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class CheckAuthorizedUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onComplete: (Boolean) -> Unit) {
        return userRepository.checkAuthorized(onComplete)
    }
}