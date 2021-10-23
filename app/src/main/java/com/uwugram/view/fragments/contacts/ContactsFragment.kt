package com.uwugram.view.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uwugram.R
import com.uwugram.database.REF_NODE_USER_CONTACTS
import com.uwugram.database.getContactsListListener
import com.uwugram.databinding.FragmentContactsBinding
import com.uwugram.utils.AppValueEventListener
import com.uwugram.utils.fadeIn
import com.uwugram.utils.fadeOut
import com.uwugram.view.fragments.AbstractFragment

class ContactsFragment : AbstractFragment(R.layout.fragment_contacts) {

    private lateinit var binding: FragmentContactsBinding
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

        val adapter = ContactsAdapter().also {
            binding.contactsRecycleView.adapter = it
        }

        contactsListener = getContactsListListener(
            onNoItemsFound = {
                binding.noContactsMessage.visibility = View.VISIBLE
                binding.noContactsMessage.fadeIn()
                binding.loaderAnimation.fadeOut()
            },
            onItemFound = { contact ->
                adapter.updateListItem(contact)
            },
            onComplete = {
                binding.contactsRecycleView.fadeIn()
                binding.loaderAnimation.fadeOut()
            }

        )

        REF_NODE_USER_CONTACTS.addValueEventListener(contactsListener)
    }

    override fun onStop() {
        super.onStop()
        REF_NODE_USER_CONTACTS.removeEventListener(contactsListener)
    }
}
