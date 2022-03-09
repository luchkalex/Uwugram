package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class SendVerificationCodeUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(
        phoneNumber: String,
        activity: Any,
        onCodeSend: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        return userRepository.sendVerificationCode(phoneNumber, activity, onCodeSend, onFailed)
    }
}