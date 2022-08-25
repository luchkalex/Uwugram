package com.uwugram.presentation.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.domain.models.Message
import com.uwugram.domain.models.MessageTypes
import com.uwugram.domain.models.User
import com.uwugram.domain.usecases.GetContactInfoUsecase
import com.uwugram.domain.usecases.GetMessagesUsecase
import com.uwugram.domain.usecases.SendMessageUsecase
import com.uwugram.utils.addUniqueItemToList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@HiltViewModel
class SingleChatViewModel @Inject constructor(
    private val getContactInfoUsecase: GetContactInfoUsecase,
    private val getMessagesUsecase: GetMessagesUsecase,
    private val sendMessageUsecase: SendMessageUsecase,
) : ViewModel() {

    private val _messagesList: MutableList<Message> = mutableStateListOf()
    val messageList: List<Message> = _messagesList

    private val _contact = MutableLiveData<User>()
    val contact: LiveData<User> = _contact

    var showNoMessagesMessage = mutableStateOf(false)
        private set

    private var _messageCount = Int.MAX_VALUE

    private var initialized = false

    private var userID: String = ""


    fun getContent(userID: String) {
        if (!initialized) {
            initialized = true
            this.userID = userID
            getContactInfoUsecase.execute(userID) { contact ->
                _contact.value = contact
            }
            getMessages()
        }
    }

    fun send(text: String, onSuccess: () -> Unit) {
        sendMessageUsecase.execute(
            message = text,
            contactId = userID,
            messageType = MessageTypes.TEXT
        ) {
            _messageCount++
            onSuccess()
        }
    }

    private fun getMessages() {
        getMessagesUsecase.execute(
            userId = userID,
            onNoItemsFound = {
                _messageCount = 0
                showNoMessagesMessage.value = true
            },
            onItemFound = { message ->
                _messagesList.addUniqueItemToList(message)
                _messagesList.sortByDescending {
                    it.timestamp.toString()
                }
                showNoMessagesMessage.value = false
            },
            onComplete = { _messageCount = it }
        )
//        getCurrentMessageCount(
//            userID,
//            onNoMessagesFound = { _messageCount = 0 },
//            onComplete = { messageCount ->
//                _messageCount = messageCount
//                REF_NODE_USER_MESSAGES.child(userID)
//                    .addChildEventListener(getMessagesListener() { message ->
//                        _messagesList.addUniqueItemToList(message)
//                        _messagesList.sortByDescending {
//                            it.timestamp.toString()
//                        }
//                    })
//            },
//        )
    }
}
//
//class SingleChatViewModelFactory(
//    private val userID: String,
//    private val getContactInfoUsecase: GetContactInfoUsecase,
//    private val getMessagesUsecase: GetMessagesUsecase,
//    private val sendMessageUsecase: SendMessageUsecase,
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return modelClass.getConstructor(String::class.java)
//            .newInstance(userID, getContactInfoUsecase, getMessagesUsecase, sendMessageUsecase)
//    }
//}