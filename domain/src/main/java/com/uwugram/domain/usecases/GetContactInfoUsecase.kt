package com.uwugram.domain.usecases

import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class GetContactInfoUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(userID: String, onComplete: (User) -> Unit) {
        return userRepository.getContactInfo(userID, onComplete)
    }
}