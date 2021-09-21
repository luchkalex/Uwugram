package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uwugram.R
import com.uwugram.databinding.FragmentEditNameBinding
import com.uwugram.utils.*

class EditNameFragment : AbstractFragment(R.layout.fragment_edit_name) {

    private var _binding: FragmentEditNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.edit_name_activity_title)
        val fullNameList = USER.fullName.split(" ")
        binding.editNameInputField.setText(fullNameList[0])
        binding.editSurnameInputField.setText(fullNameList[1])
        binding.editNameConfirmFab.setOnClickListener { updateName() }
    }

    private fun updateName() {
        var fullName = binding.editNameInputField.text.toString()
        if (fullName.isEmpty()) {
            showShortToast("First name is required")
        } else {
            fullName = "$fullName ${binding.editSurnameInputField.text.toString()}"
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FIELD_USERS_FULLNAME)
                .setValue(fullName).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showShortToast("Name updated")
                        USER.fullName = fullName
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
        }
    }
}