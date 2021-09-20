package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.databinding.FragmentEnterPhoneNumberBinding
import com.uwugram.utils.replaceFragment
import com.uwugram.utils.showShortToast

class EnterPhoneNumberFragment : Fragment() {

    private var _binding: FragmentEnterPhoneNumberBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.confirmFab.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (binding.phoneNumberInputField.text.toString().length < 9) {
            showShortToast("Enter phone number")
        } else {
            replaceFragment(R.id.loginFragmentContainer, CodeVerificationFragment())
        }
    }
}