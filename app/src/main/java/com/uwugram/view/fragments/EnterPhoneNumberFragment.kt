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
import com.uwugram.databinding.FragmentEnterPhoneNumberBinding
import com.uwugram.utils.AUTH
import com.uwugram.utils.initFirebase
import com.uwugram.utils.replaceFragment
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
                replaceFragment(
                    R.id.loginFragmentContainer,
                    CodeVerificationFragment(id, phoneNumber)
                )
            }
        }
        binding.confirmFab.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        phoneNumber =
            getString(R.string.default_ukraine_country_code) + binding.phoneNumberInputField.text.toString()
        if (phoneNumber.length < 9) {
            showShortToast("Enter phone number")
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