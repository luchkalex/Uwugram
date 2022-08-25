package com.uwugram.domain.usecases

import com.uwugram.domain.models.User
import com.uwugram.domain.repositories.UserRepository

class GetContactsListUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(onNoItemsFound: () -> Unit, onItemFound: (User) -> Unit, onComplete: () -> Unit) {
        return userRepository.getContacts(onNoItemsFound, onItemFound, onComplete)
    }
}