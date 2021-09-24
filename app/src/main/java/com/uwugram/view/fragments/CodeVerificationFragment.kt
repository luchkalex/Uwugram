package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.uwugram.R
import com.uwugram.activities.ChatActivity
import com.uwugram.activities.LoginActivity
import com.uwugram.databinding.FragmentCodeVerificationBinding
import com.uwugram.model.User
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
        binding.codeVerificationInputField.addTextChangedListener(AppTextWatcher {
            if (binding.codeVerificationInputField.text.toString().length == 6)
                onEnterCode()
        })
    }

    private fun onEnterCode() {
        val code = binding.codeVerificationInputField.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                UID = AUTH.currentUser?.uid.toString()

                REF_DATABASE_ROOT.child(NODE_USERS)
                    .addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
                        if (snapshot.hasChild(UID)) {
                            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FIELD_USERS_STATUS)
                                .setValue(getString(R.string.online_status))
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showShortToast(getString(R.string.login_welcome_back_message))
                                        (activity as LoginActivity).replaceActivity(ChatActivity())
                                    }
                                }
                        } else {
                            USER = User(
                                id = UID,
                                phone = phoneNumber,
                            )
                            replaceFragment(
                                R.id.loginFragmentContainer,
                                EditNameFragment(initial = true)
                            )
                        }
                    })
            } else {
                showShortToast(getString(R.string.code_verification_wrong_code_message))
                binding.codeVerificationInputField.setText("")
            }
        }
    }
}