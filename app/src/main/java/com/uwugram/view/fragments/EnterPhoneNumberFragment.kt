package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.databinding.FragmentEnterPhoneNumberBinding
import com.uwugram.utils.AUTH
import com.uwugram.utils.initFirebase
import com.uwugram.utils.showShortToast
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment() {

    private var _binding: FragmentEnterPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var phoneNumber: String

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
        initFirebase()
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showShortToast(p0.message.toString())
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
        binding.enterPhoneConfirmFab.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        phoneNumber =
            getString(R.string.enter_phone_default_ukraine_country_code) + binding.enterPhoneInputField.text.toString()
        if (phoneNumber.length < 13) {
            showShortToast(getString(R.string.enter_phone_number_too_short_message))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        activity?.let {
            PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder(AUTH)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60, TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callback)
                    .build()
            )
        }
    }
}