package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class SetUserImageUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(uri: String) {
        return userRepository.setUserImage(uri)
    }
}