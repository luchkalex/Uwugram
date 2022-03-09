package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class GetFullNameUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onComplete: (String) -> Unit) {
        return userRepository.getFullName(onComplete)
    }
}