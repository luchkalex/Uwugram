package com.uwugram.view.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.uwugram.R
import com.uwugram.databinding.FragmentContactsBinding
import com.uwugram.utils.*
import com.uwugram.view.fragments.AbstractFragment

class ContactsFragment : AbstractFragment(R.layout.fragment_contacts) {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactsAdapter
    private lateinit var refContacts: DatabaseReference
    private lateinit var refUsers: DatabaseReference
    private lateinit var contactsListener: AppValueEventListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.contacts_activity_title)
        binding.loaderAnimation.visibility = View.VISIBLE
        binding.contactsRecycleView.visibility = View.GONE
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.contactsRecycleView

        refContacts = REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(USER.id)
        refUsers = REF_DATABASE_ROOT.child(NODE_USERS)

        adapter = ContactsAdapter()
        recyclerView.adapter = adapter

        contactsListener = getContactsListener()

        refContacts.addValueEventListener(contactsListener)
    }

    private fun getContactsListener() = AppValueEventListener { contacts ->
        if (contacts.children.count() > 0) {
            contacts.children.map { contact ->

                val currentContact = contact.getUserModel()
                refUsers.child(contact.key.toString())
                    .addListenerForSingleValueEvent(AppValueEventListener { contactInfo ->
                        currentContact.photoURL =
                            contactInfo.child(FIELD_USERS_PHOTO_URL).value.toString()
                        currentContact.state = contactInfo.child(FIELD_USERS_STATE).value.toString()
                        adapter.updateListItem(currentContact)
                        binding.contactsRecycleView.fadeIn()
                        binding.loaderAnimation.fadeOut()
                    })
            }
        } else {
            binding.noContactsMessage.visibility = View.VISIBLE
            binding.noContactsMessage.fadeIn()
            binding.loaderAnimation.fadeOut()
        }
    }

    override fun onStop() {
        super.onStop()
        refContacts.removeEventListener(contactsListener)
    }
}
