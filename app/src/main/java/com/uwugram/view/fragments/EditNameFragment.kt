package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.activities.MainActivity
import com.uwugram.database.USER
import com.uwugram.database.singUp
import com.uwugram.database.updateName
import com.uwugram.databinding.FragmentEditNameBinding
import com.uwugram.utils.MAIN_ACTIVITY
import com.uwugram.utils.hideKeyboard
import com.uwugram.utils.replaceActivity
import com.uwugram.utils.showShortToast

class EditNameFragment : Fragment() {

    private lateinit var binding: FragmentEditNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MAIN_ACTIVITY.title = getString(R.string.edit_name_activity_title)
        binding.loaderAnimation.visibility = View.GONE
        binding.editNameConfirmFab.setOnClickListener { setNewName() }
        initInputFieldsText()
    }

    private fun initInputFieldsText() {
        val fullNameList = USER.fullName.split(" ")
        binding.editNameInputField.setText(fullNameList[0])
        if (fullNameList.size > 1) binding.editSurnameInputField.setText(fullNameList[1])
    }

    private fun setNewName() {
        val oldName = USER.fullName
        val fullName = binding.editNameInputField.text.toString()
        val initial = arguments?.getBoolean("initial") ?: false

        if (fullName.isEmpty()) {
            showShortToast(getString(R.string.edit_name_name_required_message))
        } else {
            USER.fullName = "$fullName ${binding.editSurnameInputField.text.toString()}"
            binding.loaderAnimation.visibility = View.VISIBLE
            binding.editNameContainer.visibility = View.GONE

            if (initial) {
                singUp(
                    onSuccess = {
                        showShortToast(getString(R.string.login_welcome_message))
                        (activity as LoginActivity).replaceActivity(MainActivity())
                    },
                    onFailure = { message ->
                        binding.loaderAnimation.visibility = View.GONE
                        binding.editNameContainer.visibility = View.VISIBLE
                        showShortToast(message)
                    }
                )
            } else {
                updateName(
                    onSuccess = {
                        MAIN_ACTIVITY.appDrawer.updateHeader()
                        showShortToast(getString(R.string.edit_name_name_updated_message))
                        activity?.let { hideKeyboard(it) }
                        MAIN_ACTIVITY.navController.popBackStack()
                    },
                    onFailure = { message ->
                        binding.loaderAnimation.visibility = View.GONE
                        binding.editNameContainer.visibility = View.VISIBLE
                        USER.fullName = oldName
                        showShortToast(message)
                    }
                )
            }
        }
    }
}