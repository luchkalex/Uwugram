package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class GetBioUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onComplete: (String) -> Unit) {
        return userRepository.getBio(onComplete)
    }
}