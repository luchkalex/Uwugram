package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.database.USER
import com.uwugram.database.checkUsernameForUniqueness
import com.uwugram.databinding.FragmentEditUsernameBinding
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.hideKeyboard
import com.uwugram.utils.showShortToast

class EditUsernameFragment : Fragment() {

    private lateinit var binding: FragmentEditUsernameBinding
    private lateinit var newUsername: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MAIN_ACTIVITY.title = getString(R.string.edit_username_activity_title)
        binding.editUsernameInputField.setText(USER.username)

        binding.editUsernameConfirmFab.setOnClickListener {
            updateUsername()
        }
    }

    private fun updateUsername() {
        newUsername = binding.editUsernameInputField.text.toString()
        if (verifyUsername()) {
            checkUsernameForUniqueness(
                newUsername,
                onUsernameAlreadyTaken = {
                    showShortToast(getString(R.string.edit_username_username_taken_message))
                },
                onUsernameVerified = {
                    showShortToast(getString(R.string.edit_username_name_updated_message))
                    MAIN_ACTIVITY.navController.popBackStack()
                    hideKeyboard(MAIN_ACTIVITY)
                }
            )
        }
    }

    private fun verifyUsername(): Boolean {
        val regex = Regex("(\\w|\\d)*")
        return when {
            newUsername.isEmpty() -> {
                showShortToast(getString(R.string.edit_username_name_required_message))
                false
            }
            regex.matches(newUsername) -> {
                true
            }
            else -> {
                showShortToast(getString(R.string.edit_username_na_symbols_message))
                false
            }
        }
    }
}