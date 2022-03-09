package com.uwugram.domain.usecases

import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class GetUserUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onValueUpdated: (User) -> Unit) {
        return userRepository.getUser(onValueUpdated)
    }
}