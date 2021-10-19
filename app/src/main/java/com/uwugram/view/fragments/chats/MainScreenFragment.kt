package com.uwugram.view.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.uwugram.R
import com.uwugram.databinding.FragmentMainScreenBinding
import com.uwugram.model.User
import com.uwugram.utils.*

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding

    private lateinit var adapter: MainScreenAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesListener: AppValueEventListener
    private lateinit var refMessages: DatabaseReference
    private lateinit var refContacts: DatabaseReference
    private lateinit var refUsers: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.loaderAnimation.visibility = View.VISIBLE
        binding.chatsRecycleView.visibility = View.GONE

        if (MAIN_ACTIVITY.isAppDrawerInitialized()) {
            MAIN_ACTIVITY.appDrawer.enableDrawer()
            activity?.title = getString(R.string.app_name)
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.chatsRecycleView
        adapter = MainScreenAdapter()

        refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID)
        refContacts = REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID)
        refUsers = REF_DATABASE_ROOT.child(NODE_USERS)

        recyclerView.adapter = adapter

        messagesListener = getMessagesListener()

        refMessages.addValueEventListener(messagesListener)
    }

    private fun getMessagesListener() = AppValueEventListener { currentUserDialogs ->
        if (currentUserDialogs.children.count() > 0) {
            currentUserDialogs.children.map { dialog ->
                val dialogUser = User(id = dialog.key.toString())

                dialogUser.message = dialog.children.last().getMessageModel()

                refContacts.child(dialogUser.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { contact ->
                        dialogUser.fullName = contact.getUserModel().fullName
                        refUsers.child(contact.key.toString())
                            .addListenerForSingleValueEvent(AppValueEventListener { contactInfo ->
                                dialogUser.photoURL =
                                    contactInfo.child(FIELD_USERS_PHOTO_URL).value.toString()
                                adapter.updateListItem(dialogUser)

                                if (currentUserDialogs.children.last().key.toString() == dialogUser.id) {
                                    binding.chatsRecycleView.fadeIn()
                                    binding.loaderAnimation.fadeOut()
                                }
                            })
                    })
            }
        } else {
            binding.noConversationsMessage.fadeIn()
            binding.loaderAnimation.fadeOut()
        }
    }

    override fun onStop() {
        super.onStop()
        refMessages.removeEventListener(messagesListener)
    }
}