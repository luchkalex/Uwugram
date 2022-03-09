package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.usecases.GetFullNameUsecase
import com.uwugram.domain.usecases.SignUpUsecase
import com.uwugram.domain.usecases.UpdateFullNameUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class EditNameViewModel @Inject constructor(
    private val getFullNameUsecase: GetFullNameUsecase,
    private val updateFullNameUsecase: UpdateFullNameUsecase,
    private val signUpUsecase: SignUpUsecase,
) : ViewModel() {

    var firstName = mutableStateOf("")
        private set

    var lastName = mutableStateOf("")
        private set

    init {
        initFullName()
    }

    private fun initFullName() {
        getFullNameUsecase.execute { fullName ->
            val fullNameList = fullName.split(" ")
            this.firstName.value = fullNameList[0]
            if (fullNameList.size > 1)
                this.lastName.value = fullNameList[1]
        }
    }

    fun saveName(firstName: String, lastName: String, onSuccess: () -> Unit, onFailed: () -> Unit) {
        if (firstName.isNotEmpty()) {
            updateFullNameUsecase.execute(
                firstName.plus(" ").plus(lastName),
                onSuccess = onSuccess,
            )
        } else {
            onFailed()
        }
    }

    fun signUp(
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
    ) {
        if (firstName.isNotEmpty()) {
            signUpUsecase.execute(firstName.plus(" ").plus(lastName), onSuccess)
        } else {
            onFailed()
        }

    }


    fun onChangeFirstName(firstName: String) {
        this.firstName.value = firstName
    }

    fun onChangeLastName(lastName: String) {
        this.lastName.value = lastName
    }
}