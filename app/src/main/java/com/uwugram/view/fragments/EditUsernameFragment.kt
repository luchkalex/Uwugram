package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uwugram.R
import com.uwugram.databinding.FragmentEditUsernameBinding
import com.uwugram.utils.*

class EditUsernameFragment : AbstractFragment(R.layout.fragment_edit_username) {

    private var _binding: FragmentEditUsernameBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.edit_username_activity_title)
        binding.editUsernameInputField.setText(USER.username)

        binding.editUsernameConfirmFab.setOnClickListener {
            username = binding.editUsernameInputField.text.toString()
            if (verifyUsername()) {
                REF_DATABASE_ROOT.child(NODE_USERNAME)
                    .addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
                        if (snapshot.hasChild(username)) {
                            showShortToast(getString(R.string.edit_username_username_taken_message))
                        } else {
                            updateUsername(username)
                        }
                    })
            }
        }
    }

    private fun verifyUsername(): Boolean {
        val regex = Regex("(\\w|\\d)*")
        return when {
            username.isEmpty() -> {
                showShortToast(getString(R.string.edit_username_name_required_message))
                false
            }
            regex.matches(username) -> {
                true
            }
            else -> {
                showShortToast(getString(R.string.edit_username_na_symbols_message))
                false
            }
        }
    }

    private fun updateUsername(newUsername: String) {
        REF_DATABASE_ROOT.child(NODE_USERNAME).child(username)
            .setValue(UID).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    REF_DATABASE_ROOT.child(NODE_USERS)
                        .child(UID)
                        .child(FIELD_USERS_USERNAME)
                        .setValue(newUsername)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                deleteOldUsername()
                                USER.username = username
                            } else {
                                showShortToast(it.exception?.message.toString())
                            }
                        }
                } else {
                    showShortToast(task.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAME).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showShortToast(getString(R.string.edit_username_name_updated_message))
                    activity?.supportFragmentManager?.popBackStack()
                    activity?.let { activity -> hideKeyboard(activity) }
                } else {
                    showShortToast(it.exception?.message.toString())
                }
            }
    }
}