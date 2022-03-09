package com.uwugram.presentation.viewmodel

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.models.States
import com.uwugram.domain.models.User
import com.uwugram.domain.usecases.GetUserUsecase
import com.uwugram.domain.usecases.LogoutUsecase
import com.uwugram.domain.usecases.SetUserImageUsecase
import com.uwugram.domain.usecases.UpdateUserStateUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserUsecase: GetUserUsecase,
    private val updateUserStateUsecase: UpdateUserStateUsecase,
    private val setUserImageUsecase: SetUserImageUsecase,
    private val logoutUsecase: LogoutUsecase,
) : ViewModel() {

    var user = mutableStateOf(User())
        private set

    init {
        initUser()
    }

    private fun initUser() {
        getUserUsecase.execute { user.value = it }
    }

    fun saveImage(uri: Uri?) {
        uri?.let { setUserImageUsecase.execute(uri.toString()) }
    }

    fun logout(onComplete: () -> Unit) {
        updateUserStateUsecase.execute(States.OFFLINE)
        logoutUsecase.execute()
        onComplete()
    }
}