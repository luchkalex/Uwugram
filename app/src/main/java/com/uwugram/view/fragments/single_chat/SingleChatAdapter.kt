package com.uwugram.view.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.uwugram.R
import com.uwugram.model.Message
import com.uwugram.utils.UID
import com.uwugram.utils.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var listMessagesCache = emptyList<Message>()

    fun setList(list: List<Message>) {
        listMessagesCache = list
        notifyItemInserted(itemCount)
    }

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selfMessageContainer: ConstraintLayout = view.findViewById(R.id.self_message_container)
        val selfMessageText: TextView = view.findViewById(R.id.self_message_text)
        val selfMessageTime: TextView = view.findViewById(R.id.self_message_sent_time)

        val othersMessageContainer: ConstraintLayout =
            view.findViewById(R.id.others_message_container)
        val othersMessageText: TextView = view.findViewById(R.id.others_message_text)
        val othersMessageTime: TextView = view.findViewById(R.id.others_message_sent_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (listMessagesCache[position].sender == UID) {
            holder.selfMessageContainer.visibility = View.VISIBLE
            holder.othersMessageContainer.visibility = View.GONE
            holder.selfMessageText.text = listMessagesCache[position].text
            holder.selfMessageTime.text = listMessagesCache[position].timestamp.toString().asTime()
        } else {
            holder.selfMessageContainer.visibility = View.GONE
            holder.othersMessageContainer.visibility = View.VISIBLE
            holder.othersMessageText.text = listMessagesCache[position].text
            holder.othersMessageTime.text =
                listMessagesCache[position].timestamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int = listMessagesCache.size
}
