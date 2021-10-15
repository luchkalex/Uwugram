package com.uwugram.view.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.uwugram.R
import com.uwugram.databinding.FragmentSingleChatBinding
import com.uwugram.model.Message
import com.uwugram.model.User
import com.uwugram.utils.*
import com.uwugram.view.fragments.AbstractFragment


class SingleChatFragment : AbstractFragment(R.layout.fragment_single_chat) {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var listenerInfoToolbar: AppValueEventListener
    private lateinit var receivingUser: User
    private lateinit var refUser: DatabaseReference
    private lateinit var refMessages: DatabaseReference
    private lateinit var adapter: SingleChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesListener: AppValueEventListener
    private lateinit var contactId: String
    private var listMessages = emptyList<Message>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        contactId = arguments?.getString("id") ?: ""

        binding.sendButton.setOnClickListener {
            val message = binding.inputMessage.text.toString()
            if (message.isEmpty()) {
                showShortToast("Message is empty")
            } else {
                sendMessage(message, contactId, MessageTypes.TEXT) {
                    binding.inputMessage.setText("")
                }
            }
        }

        initToolbar(contactId)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.chatRecyclerView
        adapter = SingleChatAdapter()
        refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contactId)
        recyclerView.adapter = adapter
        messagesListener = AppValueEventListener { dataSnapshot ->
            listMessages = dataSnapshot.children.map { it.getMessageModel() }
            adapter.setList(listMessages)
            recyclerView.smoothScrollToPosition(adapter.itemCount)
        }
        refMessages.addValueEventListener(messagesListener)
    }

    private fun initToolbar(contactId: String) {
        MAIN_ACTIVITY.toolbar[0].visibility = View.VISIBLE
        listenerInfoToolbar = AppValueEventListener { dataSnapshot ->
            receivingUser = dataSnapshot.getUserModel()
            REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID).child(contactId).child(
                FIELD_USERS_FULLNAME
            )
                .addListenerForSingleValueEvent(AppValueEventListener {
                    receivingUser.fullName = it.getValue(String::class.java).toString()
                    initInfoToolbar()
                })
        }

        refUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contactId)

        refUser.addValueEventListener(listenerInfoToolbar)
    }


    private fun initInfoToolbar() {
        MAIN_ACTIVITY.chatToolbarBinding.profileImage.downloadAndSetImage(receivingUser.photoURL)
        MAIN_ACTIVITY.chatToolbarBinding.settingsFullName.text = receivingUser.fullName
        MAIN_ACTIVITY.chatToolbarBinding.activeStatus.text = receivingUser.status
    }

    override fun onStop() {
        super.onStop()
        MAIN_ACTIVITY.toolbar[0].visibility = View.GONE
        refUser.removeEventListener(listenerInfoToolbar)
        refMessages.removeEventListener(messagesListener)
    }


}