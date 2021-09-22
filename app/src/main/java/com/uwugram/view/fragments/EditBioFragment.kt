package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uwugram.R
import com.uwugram.databinding.FragmentEditBioBinding
import com.uwugram.utils.*

class EditBioFragment : AbstractFragment(R.layout.fragment_edit_bio) {
    private var _binding: FragmentEditBioBinding? = null
    private val binding get() = _binding!!
    private lateinit var bio: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.edit_bio_activity_title)
        binding.editBioInputField.setText(USER.bio)

        binding.editBioConfirmFab.setOnClickListener {
            bio = binding.editBioInputField.text.toString()

            if (bio.isEmpty()) {
                showShortToast(getString(R.string.edit_bio_required_message))
            } else {
                REF_DATABASE_ROOT.child(NODE_USERS).child(USER.id).child(FIELD_USERS_BIO)
                    .setValue(bio).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showShortToast(getString(R.string.edit_bio_updated_message))
                            USER.bio = bio
                            activity?.supportFragmentManager?.popBackStack()
                        } else {
                            showShortToast(it.exception?.message.toString())
                        }
                    }
            }
        }
    }
}