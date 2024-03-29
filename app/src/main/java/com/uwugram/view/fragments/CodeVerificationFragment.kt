package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.activities.MainActivity
import com.uwugram.database.UID
import com.uwugram.database.USER
import com.uwugram.databinding.FragmentCodeVerificationBinding
import com.uwugram.model.User
import com.uwugram.utils.AppTextWatcher
import com.uwugram.utils.replaceActivity
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
        binding.codeVerificationInputField.addTextChangedListener(AppTextWatcher {
            if (binding.codeVerificationInputField.text.toString().length == 6)
                verifyCode()
        })
        binding.loaderAnimation.visibility = View.GONE
    }

    private fun verifyCode() {
        binding.loaderAnimation.visibility = View.VISIBLE
        binding.codeVerificationContainer.visibility = View.GONE

        val verificationCode = binding.codeVerificationInputField.text.toString()
        val id = arguments?.getString("id").toString()
        val phoneNumber = arguments?.getString("phoneNumber").toString()
        val loginActivity = (activity as LoginActivity)

        com.uwugram.database.verifyCode(
            id,
            verificationCode,
            onSignIn = {
                USER.id = UID
                loginActivity.showShortToast(loginActivity.getString(R.string.login_welcome_back_message))
                loginActivity.replaceActivity(MainActivity())
            },
            onSignUp = {
                USER = User(
                    id = UID,
                    phone = phoneNumber,
                )
                loginActivity.navController
                    .navigate(
                        R.id.action_codeVerificationFragment_to_editNameFragment,
                        Bundle().apply {
                            putBoolean("initial", true)
                        })

            },
            onWrongCode = {
                binding.loaderAnimation.visibility = View.GONE
                binding.codeVerificationContainer.visibility = View.VISIBLE
                showShortToast(getString(R.string.code_verification_wrong_code_message))
                binding.codeVerificationInputField.setText("")
            }
        )
    }
}