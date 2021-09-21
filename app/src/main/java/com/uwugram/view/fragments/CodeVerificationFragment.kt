package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.uwugram.activities.LoginActivity
import com.uwugram.activities.MainActivity
import com.uwugram.databinding.FragmentCodeVerificationBinding
import com.uwugram.utils.*

class CodeVerificationFragment(val id: String, private val phoneNumber: String) : Fragment() {

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
                onEnterCode()
        })
    }

    private fun onEnterCode() {
        val code = binding.verificationCodeInputField.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataMap = mutableMapOf<String, Any>()
                val uid = AUTH.currentUser?.uid.toString()
                dataMap[FIELD_USERS_ID] = uid
                dataMap[FIELD_USERS_PHONE] = phoneNumber
                dataMap[FIELD_USERS_USERNAME] = ""
                dataMap[FIELD_USERS_FULLNAME] = "UWU "
                dataMap[FIELD_USERS_BIO] = ""
                dataMap[FIELD_USERS_STATUS] = "online"
                dataMap[FIELD_USERS_PHOTO_URL] = ""
                REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            (activity as LoginActivity).replaceActivity(MainActivity())
                        } else {
                            showShortToast(it.exception?.message.toString())
                        }
                    }
            } else {
                showShortToast("Wrong code")
                binding.verificationCodeInputField.setText("")
            }
        }
    }
}