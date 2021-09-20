package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.databinding.FragmentCodeVerificationBinding
import com.uwugram.utils.AppTextWatcher
import com.uwugram.utils.showShortToast

class CodeVerificationFragment : Fragment() {

    private var _binding: FragmentCodeVerificationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.verificationCodeInputField.addTextChangedListener(AppTextWatcher {
            if (binding.verificationCodeInputField.text.toString().length == 6)
                verifyCode()
        })
    }

    private fun verifyCode() {
        showShortToast("Code verification")
    }
}