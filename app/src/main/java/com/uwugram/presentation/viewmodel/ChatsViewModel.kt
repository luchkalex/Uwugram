package com.uwugram.presentation.viewmodel

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.models.States
import com.uwugram.domain.models.User
import com.uwugram.domain.usecases.GetChatsUsecase
import com.uwugram.domain.usecases.GetUserUsecase
import com.uwugram.domain.usecases.UpdateContactsUsecase
import com.uwugram.domain.usecases.UpdateUserStateUsecase
import com.uwugram.utils.READ_CONTACTS
import com.uwugram.utils.addUniqueUserToList
import com.uwugram.utils.checkPermission
import com.uwugram.utils.getUserContactsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val updateUserStateUsecase: UpdateUserStateUsecase,
    getUserUsecase: GetUserUsecase,
    private val updateContactsUsecase: UpdateContactsUsecase,
    private val getChatsUsecase: GetChatsUsecase,
) : ViewModel() {

    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened: StateFlow<Boolean> = _drawerShouldBeOpened

    private val _chatList: MutableList<User> = mutableStateListOf()
    val chatList: List<User> = _chatList

    private var initializedContacts = false

    var showNoChatsMessage = mutableStateOf(false)
        private set

    var showRequestPermission = true
        private set

    var user = mutableStateOf(User())
        private set

    init {
        getUserUsecase.execute {
            if (user.value.id.isEmpty()) {
                user.value = it
                getChats()
                updateUserStateUsecase.execute(States.ONLINE)
            } else {
                user.value = it
            }

        }
    }

    private fun getChats() {
        getChatsUsecase.execute(
            onItemFound = { user ->
                _chatList.addUniqueUserToList(user)
                _chatList.sortByDescending {
                    it.message.timestamp.toString()

                }
                showNoChatsMessage.value = false
            },
            onNoItemsFound = { showNoChatsMessage.value = true },
            onComplete = {}
        )
    }

    fun initializeContacts(activity: Activity) {
        if (!initializedContacts) {
            initializedContacts = true
            if (checkPermission(READ_CONTACTS, activity)) {
                CoroutineScope(Dispatchers.IO).launch {
                    while (user.value.id.isEmpty()) {
                        delay(10)
                    }
                    updateContactsUsecase.execute(getUserContactsList(activity, user.value.phone))
                }
            }
        }
    }

    fun closePermissionScreen() {
        showRequestPermission = false
    }

    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }
}