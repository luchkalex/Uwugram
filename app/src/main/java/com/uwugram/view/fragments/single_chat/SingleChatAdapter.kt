package com.uwugram.view.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uwugram.R
import com.uwugram.database.UID
import com.uwugram.model.Message
import com.uwugram.utils.DiffMessagesUtilCallback
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.asTime
import com.uwugram.utils.setMargins

class SingleChatAdapter(private val contactID: String) :
    RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var listMessagesCache = emptyList<Message>()
    private lateinit var diffUtil: DiffUtil.DiffResult

    fun addMessage(message: Message) {
        val newList = mutableListOf<Message>()

        newList.addAll(listMessagesCache)

        if (!newList.contains(message)) {
            newList.add(message)
            newList.sortBy { it.timestamp.toString() }

            diffUtil = DiffUtil.calculateDiff(DiffMessagesUtilCallback(listMessagesCache, newList))
            diffUtil.dispatchUpdatesTo(this)
            listMessagesCache = newList
            notifyItemChanged(itemCount - 2)
        }
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
            changeBackgroundOfMessages(
                true,
                UID,
                position,
                holder.selfMessageContainer
            )
        } else {
            holder.selfMessageContainer.visibility = View.GONE
            holder.othersMessageContainer.visibility = View.VISIBLE
            holder.othersMessageText.text = listMessagesCache[position].text
            holder.othersMessageTime.text =
                listMessagesCache[position].timestamp.toString().asTime()
            changeBackgroundOfMessages(
                false,
                contactID,
                position,
                holder.othersMessageContainer
            )
        }
    }

    override fun getItemCount(): Int = listMessagesCache.size

    private fun changeBackgroundOfMessages(
        isOwn: Boolean,
        senderUID: String,
        position: Int,
        messageContainer: ConstraintLayout,
    ) {
        if (position == 0 || listMessagesCache[position - 1].sender != senderUID) {
            if (position == listMessagesCache.size - 1 || listMessagesCache[position + 1].sender != senderUID) {
                // first message type
                messageContainer.background =
                    getDrawable(
                        MAIN_ACTIVITY,
                        if (isOwn) R.drawable.background_self_single_message
                        else R.drawable.background_others_single_message
                    )
            } else {
                // first message type
                setMargins(messageContainer, t = 2, b = 0)
                messageContainer.background =
                    getDrawable(
                        MAIN_ACTIVITY,
                        if (isOwn) R.drawable.background_self_first_message
                        else R.drawable.background_others_first_message
                    )
            }
        } else {
            if (position == listMessagesCache.size - 1 || listMessagesCache[position + 1].sender != senderUID) {
                // last message type
                setMargins(messageContainer, t = 2)
                messageContainer.background =
                    getDrawable(
                        MAIN_ACTIVITY,
                        if (isOwn) R.drawable.background_self_last_message
                        else R.drawable.background_others_last_message
                    )
            } else {
                // middle message type
                setMargins(messageContainer, t = 2, b = 0)
                messageContainer.background =
                    getDrawable(
                        MAIN_ACTIVITY,
                        if (isOwn) R.drawable.background_self_middle_message
                        else R.drawable.background_others_middle_message
                    )
            }
        }

        if (position == listMessagesCache.size - 1) {
            setMargins(messageContainer, b = 6)
        }
    }
}
