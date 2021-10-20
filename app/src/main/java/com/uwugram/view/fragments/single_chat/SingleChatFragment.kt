package com.uwugram.view.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import com.google.firebase.database.DatabaseReference
import com.uwugram.R
import com.uwugram.database.*
import com.uwugram.databinding.FragmentSingleChatBinding
import com.uwugram.model.MessageTypes
import com.uwugram.model.User
import com.uwugram.utils.*
import com.uwugram.view.fragments.AbstractFragment


class SingleChatFragment : AbstractFragment(R.layout.fragment_single_chat) {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var listenerInfoToolbar: AppValueEventListener
    private lateinit var messagesListener: AppValueEventListener
    private lateinit var refContact: DatabaseReference
    private lateinit var refMessages: DatabaseReference
    private lateinit var contactId: String
    private var initialMessagesRequest = true


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
        binding.loaderAnimation.visibility = View.VISIBLE
        binding.inputMessage.isEnabled = false
        binding.sendButton.setOnClickListener(getSendButtonClickListener())

        contactId = arguments?.getString("id") ?: ""
        initialMessagesRequest = true

        initToolbar()
        initRecyclerView()
    }


    private fun getSendButtonClickListener(): (View) -> Unit = {
        val message = binding.inputMessage.text.toString()
        if (message.isEmpty()) {
            showShortToast("Message is empty")
        } else {
            sendMessage(message, contactId, MessageTypes.TEXT) {
                binding.inputMessage.setText("")
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.chatRecyclerView
        val adapter = SingleChatAdapter(contactId).also {
            binding.chatRecyclerView.adapter = it
        }
        refMessages = REF_NODE_USER_MESSAGES.child(contactId)

        messagesListener = getMessagesListener(
            onItemFound = { listMessages ->
                binding.noMessagesMessage.visibility = View.GONE
                adapter.setList(listMessages)
                recyclerView.smoothScrollToPosition(adapter.itemCount)
                binding.loaderAnimation.visibility = View.GONE
                binding.inputMessage.isEnabled = true
                if (initialMessagesRequest) {
                    binding.chatRecyclerView.fadeIn()
                    binding.loaderAnimation.fadeOut()
                    initialMessagesRequest = false
                }
            },
            onNoItemsFound = {
                binding.inputMessage.isEnabled = true
                binding.noMessagesMessage.visibility = View.VISIBLE
                binding.noMessagesMessage.fadeIn()
                binding.loaderAnimation.fadeOut()
            }
        )

        refMessages.addValueEventListener(messagesListener)
    }


    private fun initToolbar() {
        refContact = REF_NODE_USERS.child(contactId)
        MAIN_ACTIVITY.toolbar[0].visibility = View.VISIBLE

        listenerInfoToolbar = getContactListener { contact ->
            updateToolbar(contact)
        }

        refContact.addValueEventListener(listenerInfoToolbar)
    }


    private fun updateToolbar(receivingUser: User) {
        MAIN_ACTIVITY.chatToolbarBinding.profileImage.downloadAndSetImage(receivingUser.photoURL)
        MAIN_ACTIVITY.chatToolbarBinding.settingsFullName.text = receivingUser.fullName
        MAIN_ACTIVITY.chatToolbarBinding.activeStatus.text = receivingUser.state
    }

    override fun onStop() {
        super.onStop()
        MAIN_ACTIVITY.toolbar[0].visibility = View.GONE
        refContact.removeEventListener(listenerInfoToolbar)
        refMessages.removeEventListener(messagesListener)
    }
}