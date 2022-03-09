package com.uwugram.domain.usecases

import com.uwugram.domain.repositories.UserRepository

class VerifyCodeUsecase(
    private val userRepository: UserRepository,
) {
    fun execute(
        id: String,
        verificationCode: String,
        onLoggedIn: () -> Unit,
        onSigningUp: () -> Unit,
        onWrongCode: () -> Unit
    ) {
        return userRepository.verifyCode(id, verificationCode, onLoggedIn, onSigningUp, onWrongCode)
    }
}