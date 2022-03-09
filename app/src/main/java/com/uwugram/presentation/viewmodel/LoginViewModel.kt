package com.uwugram.presentation.viewmodel

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.usecases.SendVerificationCodeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sendVerificationCodeUsecase: SendVerificationCodeUsecase
) : ViewModel() {

    fun sendCode(
        phone: String,
        onCodeSend: (String) -> Unit,
        onFailed: () -> Unit,
        activity: Activity
    ) {
        val phoneNumber = "+380$phone"
        if (phoneNumber.length < 13) {
            onFailed()
        } else {
            sendVerificationCodeUsecase.execute(
                phoneNumber = phoneNumber,
                activity = activity,
                onCodeSend = onCodeSend,
                onFailed = onFailed
            )
        }
    }
}