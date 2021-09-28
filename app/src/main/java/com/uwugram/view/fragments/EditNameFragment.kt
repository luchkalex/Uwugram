package com.uwugram.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.activities.MainActivity
import com.uwugram.databinding.FragmentEditNameBinding
import com.uwugram.utils.*

class EditNameFragment : Fragment() {

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

        initInputFieldsText()

        binding.editNameConfirmFab.setOnClickListener { updateName() }
    }

    private fun initInputFieldsText() {
        val fullNameList = USER.fullName.split(" ")
        binding.editNameInputField.setText(fullNameList[0])
        if (fullNameList.size > 1) binding.editSurnameInputField.setText(fullNameList[1])
    }

    private fun updateName() {
        var fullName = binding.editNameInputField.text.toString()
        val initial = arguments?.getBoolean("initial") ?: false

        if (fullName.isEmpty()) {
            showShortToast(getString(R.string.edit_name_name_required_message))
        } else {
            fullName = "$fullName ${binding.editSurnameInputField.text.toString()}"
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(FIELD_USERS_FULLNAME)
                .setValue(fullName).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        USER.fullName = fullName
                        if (initial) {
                            initialSaveDataToDB()
                        } else {
                            MAIN_ACTIVITY.appDrawer.updateHeader()
                            showShortToast(getString(R.string.edit_name_name_updated_message))
                            activity?.let { hideKeyboard(it) }
                            MAIN_ACTIVITY.navController.popBackStack()
                        }
                    }
                }
        }
    }

    private fun initialSaveDataToDB() {
        val dataMap = mutableMapOf<String, Any>()
        dataMap[FIELD_USERS_ID] = UID
        dataMap[FIELD_USERS_PHONE] = USER.phone
        REF_DATABASE_ROOT.child(NODE_PHONE).child(USER.phone).setValue(UID)
            .addOnSuccessListener {
                REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                    .updateChildren(dataMap)
                    .addOnSuccessListener {
                        showShortToast(getString(R.string.login_welcome_message))
                        (activity as LoginActivity).replaceActivity(MainActivity())
                    }
            }
    }
}