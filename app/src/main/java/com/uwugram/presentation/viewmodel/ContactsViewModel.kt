package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.models.User
import com.uwugram.domain.usecases.GetContactsListUsecase
import com.uwugram.utils.addUniqueUserToList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsListUsecase: GetContactsListUsecase
) : ViewModel() {

    private val _contactsList: MutableList<User> = mutableStateListOf()
    val contactsList: List<User> = _contactsList

    var showNoContactsMessage = mutableStateOf(false)
        private set

    init {
        getContacts()
    }

    private fun getContacts() {
        getContactsListUsecase.execute(
            onNoItemsFound = {
                showNoContactsMessage.value = true
            },
            onItemFound = { user ->
                _contactsList.addUniqueUserToList(user)
                _contactsList.sortByDescending {
                    it.message.timestamp.toString()
                }
                showNoContactsMessage.value = false
            },
            onComplete = {}
        )
    }
}