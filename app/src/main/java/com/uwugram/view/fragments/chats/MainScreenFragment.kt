package com.uwugram.view.fragments.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.database.REF_NODE_USER_MESSAGES
import com.uwugram.database.getChatsListener
import com.uwugram.databinding.FragmentMainScreenBinding
import com.uwugram.utils.AppValueEventListener
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.fadeIn
import com.uwugram.utils.fadeOut

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var chatsListener: AppValueEventListener

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
            MAIN_ACTIVITY.title = getString(R.string.app_name)
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {

        val adapter = MainScreenAdapter().also {
            binding.chatsRecycleView.adapter = it
        }

        chatsListener = getChatsListener(
            onNoItemsFound = {
                binding.noConversationsMessage.fadeIn()
                binding.loaderAnimation.fadeOut()
            },
            onItemFound = { dialog ->
                adapter.updateListItem(dialog)
            },
            onComplete = {
                binding.chatsRecycleView.fadeIn()
                binding.loaderAnimation.fadeOut()
            }
        )

        REF_NODE_USER_MESSAGES.addValueEventListener(chatsListener)
    }

    override fun onStop() {
        super.onStop()
        REF_NODE_USER_MESSAGES.removeEventListener(chatsListener)
    }
}