package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.databinding.FragmentChatBinding
import com.uwugram.utils.MAIN_ACTIVITY

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (MAIN_ACTIVITY.isAppDrawerInitialized()) {
            MAIN_ACTIVITY.appDrawer.enableDrawer()
            activity?.title = getString(R.string.app_name)
        }
    }
}