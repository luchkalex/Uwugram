package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.models.States
import com.uwugram.domain.usecases.CheckAuthorizedUsecase
import com.uwugram.domain.usecases.UpdateUserStateUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkAuthorizedUsecase: CheckAuthorizedUsecase,
    private val updateUserStateUsecase: UpdateUserStateUsecase,
) : ViewModel(), DefaultLifecycleObserver {

    private var authorized = false

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        updateState(States.ONLINE)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        updateState(States.OFFLINE)
    }

    private fun checkOnAuthorized(onComplete: (Boolean) -> Unit) {
        checkAuthorizedUsecase.execute { authorized ->
            onComplete(authorized)
        }
    }

    private fun updateState(state: States) {
        checkOnAuthorized { authorized ->
            this.authorized = authorized
            if (authorized) {
                updateUserStateUsecase.execute(state)
            }
        }
    }
}