package com.uwugram.domain.usecases

import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class UpdateContactsUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(userContactsList: ArrayList<User>) {
        return userRepository.updateContacts(userContactsList)
    }
}