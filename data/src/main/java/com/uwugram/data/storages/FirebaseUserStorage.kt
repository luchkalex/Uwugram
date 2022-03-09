package com.uwugram.data.storages

import AppChildEventListener
import AppValueEventListener
import android.app.Activity
import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uwugram.data.UserStorage
import com.uwugram.domain.models.Message
import com.uwugram.domain.models.MessageTypes
import com.uwugram.domain.models.User
import java.util.concurrent.TimeUnit

const val NODE_USERS = "users"
const val NODE_USERNAME = "username"
const val NODE_PHONE = "phone"
const val NODE_PHONE_CONTACTS = "phoneContacts"
const val NODE_MESSAGES = "messages"

const val FOLDER_PROFILE_IMAGES = "profileImages"

const val FIELD_USERS_ID = "id"
const val FIELD_USERS_PHONE = "phone"
const val FIELD_USERS_USERNAME = "username"
const val FIELD_USERS_FULLNAME = "fullName"
const val FIELD_USERS_BIO = "bio"
const val FIELD_USERS_STATE = "state"
const val FIELD_USERS_PHOTO_URL = "photoURL"

const val FIELD_SENDER = "sender"
const val FIELD_TEXT = "text"
const val FIELD_TYPE = "type"
const val FIELD_TIMESTAMP = "timestamp"


class FirebaseUserStorage : UserStorage {
    private var mapValueEventListeners =
        arrayListOf<Pair<DatabaseReference, AppValueEventListener>>()
    private var mapChildEventListeners = arrayListOf<Pair<DatabaseReference, ChildEventListener>>()

    private var currentUser = User()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var dbRefRoot: DatabaseReference
    private var storageRefRoot: StorageReference
    private var dbRefNodeUserMessages: DatabaseReference
    private var dbRefNodeUserContacts: DatabaseReference
    private var dbRefNodeUsers: DatabaseReference

    init {
        auth.currentUser?.let { currentUser.id = it.uid }
        dbRefRoot = FirebaseDatabase.getInstance().reference
        storageRefRoot = FirebaseStorage.getInstance().reference
        dbRefNodeUserMessages = dbRefRoot.child(NODE_MESSAGES).child(currentUser.id)
        dbRefNodeUserContacts = dbRefRoot.child(NODE_PHONE_CONTACTS).child(currentUser.id)
        dbRefNodeUsers = dbRefRoot.child(NODE_USERS)
    }

    override fun updateUserState(state: String) {
        getRefToUserById(currentUser.id).child(FIELD_USERS_STATE).setValue(state)
    }

    override fun updateUserContacts(userContacts: ArrayList<User>) {
        dbRefRoot.child(NODE_PHONE).addListenerForSingleValueEvent(AppValueEventListener {
            dbRefNodeUserContacts.removeValue()
            it.children.forEach { dataSnapshot ->
                userContacts.forEach { contact ->
                    if (dataSnapshot.key == contact.phone) {
                        dbRefNodeUserContacts
                            .child(dataSnapshot.value.toString()).child(FIELD_USERS_ID)
                            .setValue(dataSnapshot.value.toString())
                        dbRefNodeUserContacts
                            .child(dataSnapshot.value.toString()).child(FIELD_USERS_FULLNAME)
                            .setValue(contact.fullName)
                    }
                }
            }
            userContacts.clear()
        })
    }

    override fun getUser(onValueUpdated: (User) -> Unit) {
        val userDatabaseReference: DatabaseReference = dbRefNodeUsers.child(currentUser.id)
        val userListener: AppValueEventListener = getUserListener { user ->
            onValueUpdated(user)
            currentUser = user
        }

        userDatabaseReference.addValueEventListener(userListener)

        mapValueEventListeners.add(Pair(userDatabaseReference, userListener))

    }

    override fun initializeUser(onComplete: (User) -> Unit) {
        dbRefNodeUsers.child(currentUser.id).addListenerForSingleValueEvent(AppValueEventListener {
            val user = it.getUserModel()
            onComplete(user)
            currentUser = user
        })
    }

    override fun getChats(
        onChatFound: (User) -> Unit,
        onNoItemsFound: () -> Unit,
        onComplete: () -> Unit
    ) {
        val chatsListener: AppValueEventListener = getChatsListener(
            onNoItemsFound = onNoItemsFound,
            onItemFound = onChatFound,
            onComplete = onComplete
        )
        dbRefNodeUserMessages.addValueEventListener(chatsListener)

        mapValueEventListeners.add(Pair(dbRefNodeUserMessages, chatsListener))
    }

    override fun checkAuthorized(onComplete: (Boolean) -> Unit) {
        if (currentUser.id.isNotEmpty()) {
            onComplete(true)
        } else {
            val userIdDatabaseReference: DatabaseReference =
                getRefToUserById(currentUser.id).child(FIELD_USERS_ID)
            val authorizedListener = AppValueEventListener {
                if (it.value != null) onComplete(true)
                else onComplete(false)
            }
            userIdDatabaseReference.addValueEventListener(authorizedListener)
            mapValueEventListeners.add(Pair(userIdDatabaseReference, authorizedListener))
        }
    }

    override fun setUserImage(uri: String) {
        val storageRef = storageRefRoot.child(FOLDER_PROFILE_IMAGES).child(currentUser.id)
        putImageToStorage(Uri.parse(uri), storageRef) {
            getImageUrl(storageRef) { url ->
                updatePhotoUrl(url) {}
            }
        }
    }

    override fun logout() {
        auth.signOut()
        currentUser = User()
        mapValueEventListeners.forEach { it.first.removeEventListener(it.second) }
        mapChildEventListeners.forEach { it.first.removeEventListener(it.second) }
    }

    override fun sendVerificationCode(
        phoneNumber: String,
        activity: Any,
        onCodeSend: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60, TimeUnit.SECONDS)
                .setActivity(activity as Activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                    override fun onVerificationFailed(p0: FirebaseException) {
                        onFailed()
                    }

                    override fun onCodeSent(
                        id: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        onCodeSend(id)
                        currentUser.phone = phoneNumber
                    }
                }
                )
                .build()
        )
    }

    override fun verifyCode(
        id: String,
        verificationCode: String,
        onLoggedIn: () -> Unit,
        onSigningUp: () -> Unit,
        onWrongCode: () -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(id, verificationCode)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                currentUser.id = auth.currentUser?.uid.toString()

                dbRefNodeUsers
                    .addListenerForSingleValueEvent(AppValueEventListener { users ->
                        if (users.hasChild(currentUser.id)) {
                            onLoggedIn()
                        } else {
                            onSigningUp()
                        }
                        reInitializeDatabase()
                    })
            } else {
                onWrongCode()
            }
        }
    }

    override fun getContacts(
        onNoItemsFound: () -> Unit,
        onItemFound: (User) -> Unit,
        onComplete: () -> Unit
    ) {
        val contactsListener: AppValueEventListener = getContactsListListener(
            onNoItemsFound = onNoItemsFound,
            onItemFound = onItemFound,
            onComplete = onComplete
        )
        dbRefNodeUserContacts.addValueEventListener(contactsListener)

        mapValueEventListeners.add(Pair(dbRefNodeUserContacts, contactsListener))
    }

    override fun getContactInfo(userId: String, onComplete: (User) -> Unit) {
        val contactListener = getContactListener(onComplete)
        val dbRefContact =
            dbRefNodeUsers.child(userId).also { it.addValueEventListener(contactListener) }
        mapValueEventListeners.add(Pair(dbRefContact, contactListener))
    }

    override fun getMessages(
        userId: String,
        onNoItemsFound: () -> Unit,
        onItemFound: (Message) -> Unit,
        onComplete: (Int) -> Unit,
    ) {
        getCurrentMessageCount(
            userId,
            onNoMessagesFound = onNoItemsFound,
            onComplete = { messageCount ->
                onComplete(messageCount)
                val messagesListener = getMessagesListener(onItemFound)
                dbRefNodeUserMessages.child(userId)
                    .addChildEventListener(messagesListener)
                mapChildEventListeners.add(Pair(dbRefNodeUserMessages, messagesListener))
            },
        )
    }

    override fun sendMessage(
        message: String,
        contactId: String,
        messageType: MessageTypes,
        onSuccess: () -> Unit
    ) {
        val refDialogUser = "$NODE_MESSAGES/${currentUser.id}/$contactId"
        val refDialogReceivingUser = "$NODE_MESSAGES/$contactId/${currentUser.id}"

        val messageKey = dbRefRoot.child(refDialogUser).push().key

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[FIELD_SENDER] = currentUser.id
        mapMessage[FIELD_TYPE] = messageType.value
        mapMessage[FIELD_TEXT] = message
        mapMessage[FIELD_TIMESTAMP] = ServerValue.TIMESTAMP

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        dbRefRoot.updateChildren(mapDialog).addOnSuccessListener {
            onSuccess()
        }

    }

    override fun getFullName(onComplete: (String) -> Unit) {
        onComplete(currentUser.fullName)
    }

    override fun updateFullName(fullName: String, onSuccess: () -> Unit) {
        getRefToUserById(currentUser.id).child(FIELD_USERS_FULLNAME)
            .setValue(fullName).addOnSuccessListener {
                onSuccess()
            }
    }

    override fun signUp(fullName: String, onComplete: () -> Unit) {
        val dataMap = mutableMapOf<String, Any>()
        currentUser.fullName = fullName
        dataMap[FIELD_USERS_ID] = currentUser.id
        dataMap[FIELD_USERS_PHONE] = currentUser.phone
        dataMap[FIELD_USERS_FULLNAME] = currentUser.fullName

        dbRefRoot.child(NODE_PHONE).child(currentUser.phone).setValue(currentUser.id)
            .addOnSuccessListener {
                dbRefRoot.child(NODE_USERS).child(currentUser.id)
                    .updateChildren(dataMap)
                    .addOnSuccessListener {
                        onComplete()
                    }
            }
    }

    override fun getUsername(onComplete: (String) -> Unit) {
        onComplete(currentUser.username)
    }

    override fun updateUsername(
        username: String,
        onSuccess: () -> Unit,
        onUsernameAlreadyTaken: () -> Unit
    ) {
        dbRefRoot.child(NODE_USERNAME)
            .addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
                if (snapshot.hasChild(username)) {
                    onUsernameAlreadyTaken()
                } else {
                    onUsernameVerified(username, onSuccess)
                }
            })
    }

    override fun updateBio(bio: String, onSuccess: () -> Unit) {
        getRefToUserById(currentUser.id).child(FIELD_USERS_BIO)
            .setValue(bio).addOnSuccessListener {
                onSuccess()
            }
    }

    override fun getBio(onComplete: (String) -> Unit) {
        onComplete(currentUser.bio)
    }

    private inline fun onUsernameVerified(
        username: String,
        crossinline onSuccess: () -> Unit,
    ) {
        val oldUsername = currentUser.username
        dbRefRoot.child(NODE_USERNAME).child(username)
            .setValue(currentUser.id).addOnSuccessListener {
                getRefToUserById(currentUser.id)
                    .child(FIELD_USERS_USERNAME)
                    .setValue(username)
                    .addOnSuccessListener {
                        if (oldUsername.isNotEmpty())
                            deleteOldUsername(oldUsername) {
                                onSuccess()
                            }
                    }
            }
    }

    private inline fun deleteOldUsername(oldUsername: String, crossinline onSuccess: () -> Unit) {
        dbRefRoot.child(NODE_USERNAME).child(oldUsername).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
    }

    private inline fun getMessagesListener(
        crossinline onItemAdded: (Message) -> Unit,
    ) = AppChildEventListener { message ->
        onItemAdded(message.getMessageModel())
    }

    private inline fun getCurrentMessageCount(
        contactId: String,
        crossinline onNoMessagesFound: () -> Unit,
        crossinline onComplete: (Int) -> Unit
    ) {
        dbRefNodeUserMessages.child(contactId)
            .addListenerForSingleValueEvent(AppValueEventListener { messages ->
                val messagesCount = messages.children.count()
                if (messagesCount == 0) {
                    onNoMessagesFound()
                }
                onComplete(messagesCount)
            })
    }

    private inline fun updatePhotoUrl(uri: Uri, crossinline onSuccess: () -> Unit) {
        getRefToUserById(currentUser.id).child(FIELD_USERS_PHOTO_URL)
            .setValue(uri.toString()).addOnSuccessListener {
                currentUser.photoURL = uri.toString()
                onSuccess()
            }
    }

    private inline fun getImageUrl(
        storageRef: StorageReference,
        crossinline onSuccess: (Uri) -> Unit
    ) {
        storageRef.downloadUrl.addOnSuccessListener { onSuccess(it) }
    }


    private fun reInitializeDatabase() {
        auth.currentUser?.let { currentUser.id = it.uid }
        dbRefNodeUserMessages = dbRefRoot.child(NODE_MESSAGES).child(currentUser.id)
        dbRefNodeUserContacts = dbRefRoot.child(NODE_PHONE_CONTACTS).child(currentUser.id)
        dbRefNodeUsers = dbRefRoot.child(NODE_USERS)
    }

    private inline fun putImageToStorage(
        uri: Uri?,
        storageRef: StorageReference,
        crossinline onSuccess: () -> Unit
    ) {
        uri?.let {
            storageRef.putFile(it).addOnSuccessListener { onSuccess() }
        }
    }

    private inline fun getContactListener(
        crossinline onComplete: (User) -> Unit
    ) = AppValueEventListener { dataSnapshot ->
        val receivingUser: User = dataSnapshot.getUserModel()
        dbRefNodeUserContacts.child(receivingUser.id).child(
            FIELD_USERS_FULLNAME
        ).addListenerForSingleValueEvent(AppValueEventListener {
            receivingUser.fullName = it.value.toString()
            onComplete(receivingUser)
        })
    }

    private inline fun getContactsListListener(
        crossinline onNoItemsFound: () -> Unit,
        crossinline onItemFound: (User) -> Unit,
        crossinline onComplete: () -> Unit
    ) = AppValueEventListener { contacts ->
        if (contacts.children.count() > 0) {
            contacts.children.map { contact ->
                val currentContact = contact.getUserModel()

                val contactListener = AppValueEventListener { contactInfo ->
                    currentContact.photoURL =
                        contactInfo.child(FIELD_USERS_PHOTO_URL).value.toString()
                    currentContact.state = contactInfo.child(FIELD_USERS_STATE).value.toString()
                    onItemFound(currentContact)

                    if (contacts.children.last().key.toString() == currentContact.id) {
                        onComplete()
                    }
                }

                dbRefNodeUsers.child(contact.key.toString())
                    .removeEventListener(contactListener)

                dbRefNodeUsers.child(contact.key.toString())
                    .addValueEventListener(contactListener)
            }
        } else {
            onNoItemsFound()
        }
    }

    private inline fun getChatsListener(
        crossinline onNoItemsFound: () -> Unit,
        crossinline onItemFound: (User) -> Unit,
        crossinline onComplete: () -> Unit
    ) =
        AppValueEventListener { currentUserDialogs ->
            if (currentUserDialogs.children.count() > 0) {
                currentUserDialogs.children.map { dialog ->
                    val dialogUser = User(id = dialog.key.toString())
                    val lastChild = dialog.children.last()
                    dialogUser.message = lastChild.getMessageModel()

                    val contactListener = AppValueEventListener { contact ->
                        dialogUser.fullName = contact.getUserModel().fullName

                        val userListener = AppValueEventListener { contactInfo ->
                            dialogUser.photoURL =
                                contactInfo.child(FIELD_USERS_PHOTO_URL).value.toString()

                            onItemFound(dialogUser)
                            if (currentUserDialogs.children.last().key.toString() == dialogUser.id) {
                                onComplete()
                            }
                        }

                        dbRefNodeUsers.child(contact.key.toString())
                            .removeEventListener(userListener)

                        dbRefNodeUsers.child(contact.key.toString())
                            .addValueEventListener(userListener)
                    }
                    dbRefNodeUserContacts.child(dialogUser.id)
                        .removeEventListener(contactListener)

                    dbRefNodeUserContacts.child(dialogUser.id)
                        .addValueEventListener(contactListener)
                }
            } else {
                onNoItemsFound()
            }
        }

    private inline fun getUserListener(
        crossinline onComplete: (User) -> Unit
    ) = AppValueEventListener { dataSnapshot ->
        onComplete(dataSnapshot.getUserModel())
    }

    private fun getRefToUserById(id: String): DatabaseReference {
        return dbRefNodeUsers.child(id)
    }

    private fun DataSnapshot.getUserModel(): User = getValue(User::class.java) ?: User()

    private fun DataSnapshot.getMessageModel(): Message = getValue(Message::class.java) ?: Message()
}