package com.uwugram.view.fragments

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.uwugram.activities.ChatActivity
import com.uwugram.activities.MainActivity
import com.uwugram.utils.replaceActivity

abstract class AbstractFragment : Fragment() {
    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as MainActivity).replaceActivity(ChatActivity())
        }
    }
}