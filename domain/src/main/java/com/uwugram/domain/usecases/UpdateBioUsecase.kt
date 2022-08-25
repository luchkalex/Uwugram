package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class UpdateBioUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(bio: String, onSuccess: () -> Unit) {
        return userRepository.updateBio(bio, onSuccess)
    }
}