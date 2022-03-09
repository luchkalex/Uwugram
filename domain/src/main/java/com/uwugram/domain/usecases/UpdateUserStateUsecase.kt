package com.uwugram.domain.usecases

import com.uwugram.domain.models.States
import com.uwugram.domain.repositories.UserRepository

class UpdateUserStateUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(state: States) {
        return userRepository.updateUserState(state)
    }
}