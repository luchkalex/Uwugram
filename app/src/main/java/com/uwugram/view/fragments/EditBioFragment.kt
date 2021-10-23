package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.database.USER
import com.uwugram.databinding.FragmentEditBioBinding
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.hideKeyboard
import com.uwugram.utils.showShortToast

class EditBioFragment : Fragment() {

    private lateinit var binding: FragmentEditBioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MAIN_ACTIVITY.title = getString(R.string.edit_bio_activity_title)
        binding.editBioInputField.setText(USER.bio)
        binding.editBioConfirmFab.setOnClickListener {
            updateBio()
        }
    }

    private fun updateBio() {
        val bio: String = binding.editBioInputField.text.toString()

        if (bio.isEmpty()) {
            showShortToast(getString(R.string.edit_bio_required_message))
        } else {
            com.uwugram.database.updateBio(bio) {
                showShortToast(getString(R.string.edit_bio_updated_message))
                USER.bio = bio
                hideKeyboard(MAIN_ACTIVITY)
            }
            MAIN_ACTIVITY.navController.popBackStack()
        }
    }
}