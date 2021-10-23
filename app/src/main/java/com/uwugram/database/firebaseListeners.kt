package com.uwugram.database

import com.uwugram.model.Message
import com.uwugram.model.User
import com.uwugram.utils.AppChildEventListener
import com.uwugram.utils.AppValueEventListener

inline fun getContactsListListener(
    crossinline onNoItemsFound: () -> Unit,
    crossinline onItemFound: (User) -> Unit,
    crossinline onComplete: () -> Unit
) = AppValueEventListener { contacts ->
    if (contacts.children.count() > 0) {
        contacts.children.map { contact ->

            val currentContact = contact.getUserModel()
            REF_NODE_USERS.child(contact.key.toString())
                .addListenerForSingleValueEvent(AppValueEventListener { contactInfo ->
                    currentContact.photoURL =
                        contactInfo.child(FIELD_USERS_PHOTO_URL).value.toString()
                    currentContact.state = contactInfo.child(FIELD_USERS_STATE).value.toString()
                    onItemFound(currentContact)

                    if (contacts.children.last().key.toString() == currentContact.id) {
                        onComplete()
                    }
                })
        }
    } else {
        onNoItemsFound()
    }
}

inline fun getChatsListener(
    crossinline onNoItemsFound: () -> Unit,
    crossinline onItemFound: (User) -> Unit,
    crossinline onComplete: () -> Unit
) =
    AppValueEventListener { currentUserDialogs ->
        if (currentUserDialogs.children.count() > 0) {
            currentUserDialogs.children.map { dialog ->
                val dialogUser = User(id = dialog.key.toString())

                dialogUser.message = dialog.children.last().getMessageModel()

                REF_NODE_USER_CONTACTS.child(dialogUser.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { contact ->
                        dialogUser.fullName = contact.getUserModel().fullName
                        REF_NODE_USERS.child(contact.key.toString())
                            .addListenerForSingleValueEvent(AppValueEventListener { contactInfo ->
                                dialogUser.photoURL =
                                    contactInfo.child(FIELD_USERS_PHOTO_URL).value.toString()

                                onItemFound(dialogUser)
                                if (currentUserDialogs.children.last().key.toString() == dialogUser.id) {
                                    onComplete()
                                }
                            })
                    })
            }
        } else {
            onNoItemsFound()
        }
    }

inline fun getContactListener(
    crossinline onComplete: (User) -> Unit
) = AppValueEventListener { dataSnapshot ->
    val receivingUser: User = dataSnapshot.getUserModel()
    REF_NODE_USER_CONTACTS.child(receivingUser.id).child(
        FIELD_USERS_FULLNAME
    ).addListenerForSingleValueEvent(AppValueEventListener {
        receivingUser.fullName = it.value.toString()
        onComplete(receivingUser)
    })
}

inline fun getMessagesListener(
    crossinline onItemAdded: (Message) -> Unit,
) = AppChildEventListener { message ->
    onItemAdded(message.getMessageModel())
}