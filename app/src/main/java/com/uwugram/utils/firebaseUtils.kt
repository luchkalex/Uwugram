package com.uwugram.utils

import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uwugram.model.Message
import com.uwugram.model.User
import java.util.*

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User
lateinit var UID: String


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


enum class States(val value: String) {
    ONLINE("online"),
    OFFLINE("offline"),
}

enum class MessageTypes(val value: String) {
    TEXT("text"),
}

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    if (!::USER.isInitialized) {
        USER = User()
    }
    UID = AUTH.currentUser?.uid.toString()
}

inline fun savePhotoUrlToDataBase(uri: Uri, crossinline onSuccess: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FIELD_USERS_PHOTO_URL)
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
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
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
                    REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(USER.id)
                        .child(dataSnapshot.value.toString()).child(FIELD_USERS_ID)
                        .setValue(dataSnapshot.value.toString())
                    REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(USER.id)
                        .child(dataSnapshot.value.toString()).child(FIELD_USERS_FULLNAME)
                        .setValue(contact.fullName)
                }
            }
        }
        arrayContacts.clear()
    })
}

fun updateUserStatus(status: States) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(USER.id).child(FIELD_USERS_STATE)
        .setValue(status.value)
}

fun DataSnapshot.getUserModel(): User = getValue(User::class.java) ?: User()

fun DataSnapshot.getMessageModel(): Message = getValue(Message::class.java) ?: Message()

fun sendMessage(
    message: String,
    contactId: String,
    messageType: MessageTypes,
    onSuccess: () -> Unit
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
