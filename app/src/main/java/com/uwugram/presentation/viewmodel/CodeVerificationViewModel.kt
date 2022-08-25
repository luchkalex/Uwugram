package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.uwugram.domain.usecases.VerifyCodeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class CodeVerificationViewModel @Inject constructor(
    private val verifyCodeUsecase: VerifyCodeUsecase
) : ViewModel() {

    fun verifyCode(
        id: String,
        verificationCode: String,
        onLoggedIn: () -> Unit,
        onSigningUp: () -> Unit,
        onWrongCode: () -> Unit
    ) {
        verifyCodeUsecase.execute(
            id,
            verificationCode,
            onLoggedIn,
            onSigningUp,
            onWrongCode
        )
    }
}