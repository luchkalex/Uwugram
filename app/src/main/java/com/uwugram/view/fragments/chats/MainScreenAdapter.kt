package com.uwugram.view.fragments.chats

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

class MainScreenAdapter : RecyclerView.Adapter<MainScreenAdapter.ChatHolder>() {
    private var listChatsCache = mutableListOf<User>()
    private val setChatsCache = hashSetOf<User>()


    fun updateListItem(item: User) {
        setChatsCache.add(item)
        listChatsCache = setChatsCache.toMutableList()
        listChatsCache.sortByDescending { it.message.timestamp.toString() }
        notifyItemInserted(listChatsCache.indexOf(item))
    }

    class ChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactPhoto: CircleImageView = view.findViewById(R.id.chatContactPhoto)
        val contactFullName: TextView = view.findViewById(R.id.chat_contact_fullname)
        val contactLastMessage: TextView = view.findViewById(R.id.contact_last_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_screen_item, parent, false)
        return ChatHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.contactFullName.text = listChatsCache[position].fullName
        holder.contactPhoto.downloadAndSetImage(listChatsCache[position].photoURL)
        holder.contactLastMessage.text = listChatsCache[position].message.text
        holder.itemView.setOnClickListener {
            MAIN_ACTIVITY.navController.navigate(
                R.id.action_mainScreenFragment_to_singleChatFragment,
                Bundle().apply {
                    putString("id", listChatsCache[position].id)
                }
            )
        }
    }

    override fun getItemCount(): Int = listChatsCache.size
}