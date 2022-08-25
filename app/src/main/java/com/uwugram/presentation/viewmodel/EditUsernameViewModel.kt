package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.usecases.GetUsernameUsecase
import com.uwugram.domain.usecases.UpdateUsernameUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class EditUsernameViewModel @Inject constructor(
    private val getUsernameUsecase: GetUsernameUsecase,
    private val updateUsernameUsecase: UpdateUsernameUsecase,
) : ViewModel() {

    var username = mutableStateOf("")
        private set

    init {
        initUser()
    }

    private fun initUser() {
        getUsernameUsecase.execute {
            this.username.value = it
        }
    }

    fun saveUsername(
        username: String,
        onSuccess: () -> Unit,
        onEmpty: () -> Unit,
        onNotAllowedSymbols: () -> Unit,
        onUsernameAlreadyTaken: () -> Unit
    ) {
        if (verifyUsername(username, onEmpty, onNotAllowedSymbols)) {
            updateUsernameUsecase.execute(username, onSuccess, onUsernameAlreadyTaken)
        }
    }

    private fun verifyUsername(
        username: String,
        onEmpty: () -> Unit,
        onNotAllowedSymbols: () -> Unit
    ): Boolean {
        val regex = Regex("(\\w|\\d)*")
        return when {
            username.isEmpty() -> {
                onEmpty()
                false
            }
            regex.matches(username) -> {
                true
            }
            else -> {
                onNotAllowedSymbols()
                false
            }
        }
    }

    fun onChangeUsername(username: String) {
        this.username.value = username
    }
}