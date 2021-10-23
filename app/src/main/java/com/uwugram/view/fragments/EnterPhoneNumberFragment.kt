package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.database.sendVerificationCode
import com.uwugram.databinding.FragmentEnterPhoneNumberBinding
import com.uwugram.utils.showShortToast

class EnterPhoneNumberFragment : Fragment() {

    private lateinit var binding: FragmentEnterPhoneNumberBinding
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.loaderAnimation.visibility = View.GONE

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showShortToast(p0.message.toString())
                binding.loaderAnimation.visibility = View.GONE
                binding.enterPhoneContainer.visibility = View.VISIBLE
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                (activity as LoginActivity).navController
                    .navigate(
                        R.id.action_enterPhoneNumberFragment_to_codeVerificationFragment,
                        Bundle().apply {
                            putString("phoneNumber", phoneNumber)
                            putString("id", id)
                        })
            }
        }
        binding.enterPhoneConfirmFab.setOnClickListener { sendCode(callback) }
    }

    private fun sendCode(callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        phoneNumber =
            getString(R.string.enter_phone_default_ukraine_country_code) + binding.enterPhoneInputField.text.toString()
        if (phoneNumber.length < 13) {
            showShortToast(getString(R.string.enter_phone_number_too_short_message))
        } else {
            binding.loaderAnimation.visibility = View.VISIBLE
            binding.enterPhoneContainer.visibility = View.GONE
            sendVerificationCode(phoneNumber, callback)
        }
    }
}