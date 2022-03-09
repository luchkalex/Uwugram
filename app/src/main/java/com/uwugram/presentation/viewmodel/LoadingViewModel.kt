package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.usecases.CheckAuthorizedUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val checkAuthorizedUsecase: CheckAuthorizedUsecase,
) : ViewModel() {

    private var initialized = false

    fun checkOnAuthorized(
        onAuthorized: () -> Unit,
        onNotAuthorized: () -> Unit,
    ) {
        if (!initialized) {
            initialized = true
            checkAuthorizedUsecase.execute { authorized ->
                if (authorized) {
                    onAuthorized()
                } else onNotAuthorized()
            }
        }
    }
}