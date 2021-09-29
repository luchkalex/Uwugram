package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.uwugram.R
import com.uwugram.databinding.FragmentContactsBinding
import com.uwugram.utils.*
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : AbstractFragment(R.layout.fragment_contacts) {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirebaseRecyclerAdapter<String, ContactsHolder>
    private lateinit var dbRefContacts: DatabaseReference

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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.contactsRecycleView
        dbRefContacts = REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(USER.id)

        val options = FirebaseRecyclerOptions.Builder<String>()
            .setQuery(dbRefContacts, String::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<String, ContactsHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                return ContactsHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.contact_item, parent, false)
                )
            }

            override fun onBindViewHolder(holder: ContactsHolder, position: Int, model: String) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(model)
                    .addValueEventListener(AppValueEventListener {
                        val contactUser = it.getUserModel()

                        holder.name.text = contactUser.fullName
                        holder.status.text = contactUser.status
                        holder.photo.downloadAndSetImage(contactUser.photoURL)
                    })
            }

        }

        recyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val status: TextView = view.findViewById(R.id.contact_status)
        val photo: CircleImageView = view.findViewById(R.id.contactPhoto)
    }
}
