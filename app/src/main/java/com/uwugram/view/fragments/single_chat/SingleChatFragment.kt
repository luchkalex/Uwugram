package com.uwugram.view.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.uwugram.R
import com.uwugram.database.*
import com.uwugram.databinding.FragmentSingleChatBinding
import com.uwugram.model.MessageTypes
import com.uwugram.model.User
import com.uwugram.utils.*
import com.uwugram.view.fragments.AbstractFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SingleChatFragment : AbstractFragment(R.layout.fragment_single_chat) {

    private lateinit var binding: FragmentSingleChatBinding
    private lateinit var listenerInfoToolbar: AppValueEventListener
    private lateinit var messagesListener: AppChildEventListener
    private lateinit var refContact: DatabaseReference
    private lateinit var refMessages: DatabaseReference
    private lateinit var contactId: String
    private var initialMessagesRequest = true
    private var messagesToLoad = 140
    private var isScrolling = false
    private var scrollToLastMessage = true


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
        binding.chatRecyclerView.visibility = View.INVISIBLE

        binding.inputMessage.isEnabled = false
        binding.sendButton.setOnClickListener(getSendButtonClickListener())

        contactId = arguments?.getString("id") ?: ""
        initialMessagesRequest = true

        initToolbar()
        initRecyclerView()
    }


    private fun getSendButtonClickListener(): (View) -> Unit = {
        scrollToLastMessage = true
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

        var messagesCount = 0

        getCurrentMessageCount(
            contactId,
            onNoMessagesFound = {
                binding.inputMessage.isEnabled = true
                binding.noMessagesMessage.visibility = View.VISIBLE
                binding.noMessagesMessage.fadeIn()
                binding.loaderAnimation.fadeOut()
            },
            onComplete = { count ->
                messagesCount = count
                if (messagesCount < messagesToLoad && messagesCount != 0) {
                    messagesToLoad = messagesCount
                }
                refMessages.limitToLast(messagesToLoad)
                    .addChildEventListener(messagesListener)
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isScrolling = true
                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (isScrolling && dy < 0) {
                            loadMessages(-dy)
                        }
                    }
                })
            }

        )

        messagesListener = getMessagesListener(
            onItemAdded = { message ->
                binding.noMessagesMessage.visibility = View.GONE

                adapter.addMessage(message)
                if (scrollToLastMessage) {
                    recyclerView.smoothScrollToPosition(adapter.itemCount)
                }

                if (messagesCount == 0) {
                    binding.chatRecyclerView.fadeIn()
                }

                if (initialMessagesRequest && messagesToLoad == adapter.itemCount) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(50)
                        binding.loaderAnimation.fadeOut()
                        binding.chatRecyclerView.fadeIn()
                        binding.inputMessage.isEnabled = true
                        initialMessagesRequest = false
                    }
                }
            },
        )
    }

    private fun loadMessages(addMessages: Int) {
        scrollToLastMessage = false
        isScrolling = false
        messagesToLoad += addMessages
        refMessages.limitToLast(messagesToLoad).addChildEventListener(messagesListener)
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