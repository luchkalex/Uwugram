package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.usecases.GetBioUsecase
import com.uwugram.domain.usecases.UpdateBioUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class EditBioViewModel @Inject constructor(
    private val getBioUsecase: GetBioUsecase,
    private val updateBioUsecase: UpdateBioUsecase,
) : ViewModel() {

    var bio = mutableStateOf("")
        private set

    init {
        initUser()
    }

    private fun initUser() {
        getBioUsecase.execute {
            this.bio.value = it
        }
    }

    fun saveBio(bio: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (bio.isNotEmpty()) {
            updateBioUsecase.execute(bio, onSuccess)
        } else {
            onFailure()
        }
    }

    fun onChangeBio(bio: String) {
        this.bio.value = bio
    }
}