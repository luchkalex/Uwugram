package com.uwugram.database

import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uwugram.model.Message
import com.uwugram.model.MessageTypes
import com.uwugram.model.States
import com.uwugram.model.User
import com.uwugram.utils.*
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    UID = AUTH.currentUser?.uid.toString()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    REF_NODE_USER_MESSAGES = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID)
    REF_NODE_USER_CONTACTS = REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID)
    REF_NODE_USERS = REF_DATABASE_ROOT.child(NODE_USERS)
}

fun getRefToUserById(id: String): DatabaseReference {
    return REF_NODE_USERS.child(id)
}

inline fun updatePhotoUrl(uri: Uri, crossinline onSuccess: () -> Unit) {
    getRefToUserById(UID).child(FIELD_USERS_PHOTO_URL)
        .setValue(uri.toString()).addOnSuccessListener {
            USER.photoURL = uri.toString()
            onSuccess()
        }
}

inline fun getImageUrl(storageRef: StorageReference, crossinline onSuccess: (Uri) -> Unit) {
    storageRef.downloadUrl.addOnSuccessListener { onSuccess(it) }
}

inline fun putImageToStorage(
    uri: Uri?,
    storageRef: StorageReference,
    crossinline onSuccess: () -> Unit
) {
    uri?.let {
        storageRef.putFile(it).addOnSuccessListener { onSuccess() }
    }
}

inline fun initializeUser(crossinline onComplete: () -> Unit) {
    getRefToUserById(UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(USER::class.java) ?: User()
            onComplete()
        })
}

fun isUserInitialized(): Boolean {
    return USER.id.isNotEmpty()
}

fun initContacts() {
    if (checkPermission(READ_CONTACTS)) {
        val arrayContacts = arrayListOf<User>()
        val cursor = MAIN_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null,
        )
        while (cursor?.moveToNext() == true) {
            val displayNameColumnIndex =
                cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val phoneNumberColumnIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            val fullName = cursor.getString(displayNameColumnIndex)
            var phone = cursor.getString(phoneNumberColumnIndex)

            val newUser = User().apply {
                this.fullName = fullName
                if (!phone.startsWith("+")) {
                    phone = "+38$phone"
                }
                this.phone = phone.replace(Regex("[\\s,-]"), "")
            }

            if (newUser.phone != USER.phone)
                arrayContacts.add(newUser)
        }
        cursor?.close()
        updatePhoneContacts(arrayContacts)
    } else {
        ActivityCompat.requestPermissions(
            MAIN_ACTIVITY,
            arrayOf(READ_CONTACTS),
            PermissionCodes.PERMISSION_READ_CONTACTS_REQUEST_CODE.code
        )
    }
}

fun updatePhoneContacts(arrayContacts: ArrayList<User>) {
    REF_DATABASE_ROOT.child(NODE_PHONE).addListenerForSingleValueEvent(AppValueEventListener {
        it.children.forEach { dataSnapshot ->
            arrayContacts.forEach { contact ->
                if (dataSnapshot.key == contact.phone) {
                    REF_NODE_USER_CONTACTS
                        .child(dataSnapshot.value.toString()).child(FIELD_USERS_ID)
                        .setValue(dataSnapshot.value.toString())
                    REF_NODE_USER_CONTACTS
                        .child(dataSnapshot.value.toString()).child(FIELD_USERS_FULLNAME)
                        .setValue(contact.fullName)
                }
            }
        }
        arrayContacts.clear()
    })
}

inline fun updateBio(
    bio: String,
    crossinline onSuccess: () -> Unit
) {
    getRefToUserById(UID).child(FIELD_USERS_BIO)
        .setValue(bio).addOnSuccessListener {
            onSuccess()
        }
}

fun updateUserState(status: States) {
    getRefToUserById(UID).child(FIELD_USERS_STATE)
        .setValue(status.value)
}

fun DataSnapshot.getUserModel(): User = getValue(User::class.java) ?: User()

fun DataSnapshot.getMessageModel(): Message = getValue(Message::class.java) ?: Message()

inline fun sendMessage(
    message: String,
    contactId: String,
    messageType: MessageTypes,
    crossinline onSuccess: () -> Unit
) {

    val refDialogUser = "$NODE_MESSAGES/$UID/$contactId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$contactId/$UID"

    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[FIELD_SENDER] = UID
    mapMessage[FIELD_TYPE] = messageType.value
    mapMessage[FIELD_TEXT] = message
    mapMessage[FIELD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT.updateChildren(mapDialog)
        .addOnSuccessListener { onSuccess() }
}

inline fun verifyCode(
    id: String,
    code: String,
    crossinline onWrongCode: () -> Unit,
    crossinline onSignUp: () -> Unit,
    crossinline onSignIn: () -> Unit
) {
    val credential = PhoneAuthProvider.getCredential(id, code)
    AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            UID = AUTH.currentUser?.uid.toString()

            REF_NODE_USERS
                .addListenerForSingleValueEvent(AppValueEventListener { users ->
                    if (users.hasChild(UID)) {
                        onSignIn()
                    } else {
                        onSignUp()
                    }
                })
        } else {
            onWrongCode()
        }
    }
}

inline fun updateName(
    crossinline onSuccess: () -> Unit,
    crossinline onFailure: (message: String) -> Unit,
) {
    getRefToUserById(UID).child(FIELD_USERS_FULLNAME)
        .setValue(USER.fullName).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it.message.toString())
        }
}

inline fun singUp(
    crossinline onSuccess: () -> Unit,
    crossinline onFailure: (message: String) -> Unit,
) {
    val dataMap = mutableMapOf<String, Any>()
    dataMap[FIELD_USERS_ID] = UID
    dataMap[FIELD_USERS_PHONE] = USER.phone
    dataMap[FIELD_USERS_FULLNAME] = USER.fullName

    REF_DATABASE_ROOT.child(NODE_PHONE).child(USER.phone).setValue(UID)
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                .updateChildren(dataMap)
                .addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    onFailure(it.message.toString())
                }
        }
}

inline fun updateUsername(
    newUsername: String,
    crossinline onSuccess: () -> Unit
) {
    REF_DATABASE_ROOT.child(NODE_USERNAME).child(newUsername)
        .setValue(UID).addOnSuccessListener {
            getRefToUserById(UID)
                .child(FIELD_USERS_USERNAME)
                .setValue(newUsername)
                .addOnSuccessListener {
                    if (USER.username.isNotEmpty())
                        deleteOldUsername(onSuccess)
                    USER.username = newUsername
                }
        }
}

inline fun deleteOldUsername(
    crossinline onSuccess: () -> Unit
) {
    REF_DATABASE_ROOT.child(NODE_USERNAME).child(USER.username).removeValue()
        .addOnSuccessListener {
            onSuccess()
        }
}

inline fun checkUsernameForUniqueness(
    newUsername: String,
    crossinline onUsernameAlreadyTaken: () -> Unit,
    crossinline onUsernameVerified: () -> Unit,
) {
    REF_DATABASE_ROOT.child(NODE_USERNAME)
        .addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
            if (snapshot.hasChild(newUsername)) {
                onUsernameAlreadyTaken()
            } else {
                updateUsername(newUsername) {
                    onUsernameVerified()
                }
            }
        })
}

fun sendVerificationCode(
    phoneNumber: String,
    callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
) {
    PhoneAuthProvider.verifyPhoneNumber(
        PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, SECONDS)
            .setActivity(MAIN_ACTIVITY)
            .setCallbacks(callback)
            .build()
    )
}

fun getCurrentMessageCount(
    contactId: String,
    onNoMessagesFound: () -> Unit,
    onComplete: (Int) -> Unit
) {
    REF_NODE_USER_MESSAGES.child(contactId)
        .addListenerForSingleValueEvent(AppValueEventListener { messages ->
            val messagesCount = messages.children.count()
            if (messagesCount == 0) {
                onNoMessagesFound()
            }
            onComplete(messagesCount)
        })
}