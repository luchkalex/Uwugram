package com.uwugram.view.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uwugram.R
import com.uwugram.model.User
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.downloadAndSetImage
import de.hdodenhof.circleimageview.CircleImageView

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsHolder>() {

    private var listContactsCache = mutableListOf<User>()


    fun updateListItem(item: User) {
        listContactsCache.add(item)
        notifyDataSetChanged()
    }

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.contact_fullname)
        val state: TextView = view.findViewById(R.id.contact_status)
        val photo: CircleImageView = view.findViewById(R.id.contactPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        return ContactsHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_item, parent, false)
        )
    }

    override fun getItemCount(): Int = listContactsCache.size

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        holder.name.text = listContactsCache[position].fullName
        holder.state.text = listContactsCache[position].state
        holder.photo.downloadAndSetImage(listContactsCache[position].photoURL)
        holder.itemView.setOnClickListener {
            MAIN_ACTIVITY.navController.navigate(
                R.id.action_contactsFragment_to_singleChatFragment,
                Bundle().apply {
                    putString("id", listContactsCache[position].id)
                }
            )
        }
    }
}