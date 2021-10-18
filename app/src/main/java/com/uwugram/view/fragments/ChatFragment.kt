package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.databinding.FragmentChatBinding
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.isUserInitialized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        binding.loaderAnimation.visibility = View.VISIBLE
        binding.chatsLabel.visibility = View.GONE

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                if (isUserInitialized()) {
                    binding.loaderAnimation.visibility = View.GONE
                    binding.chatsLabel.visibility = View.VISIBLE
                    break
                }
                delay(50)
            }

        }

        if (MAIN_ACTIVITY.isAppDrawerInitialized()) {
            MAIN_ACTIVITY.appDrawer.enableDrawer()
            activity?.title = getString(R.string.app_name)
        }
    }
}