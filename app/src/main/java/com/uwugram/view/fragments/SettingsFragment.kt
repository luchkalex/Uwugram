package com.uwugram.view.fragments

import android.os.Bundle
import android.view.*
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.activities.MainActivity
import com.uwugram.databinding.FragmentSettingsBinding
import com.uwugram.utils.AUTH
import com.uwugram.utils.USER
import com.uwugram.utils.replaceActivity
import com.uwugram.utils.replaceFragment

class SettingsFragment : AbstractFragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.settings_activity_title)
        binding.settingsFullName.text = USER.fullName
        binding.activeStatus.text = USER.status
        binding.settingsPhoneText.text = USER.phone

        binding.settingsBioText.text = if (USER.bio.isNotEmpty()) USER.bio else
            getString(R.string.settings_default_bio_text)

        binding.settingsUsernameText.text = if (USER.username.isNotEmpty())
            getString(R.string.username_placeholder, USER.username) else
            getString(R.string.settings_default_username_text)

        binding.settingsUsernameTile.setOnClickListener {
            replaceFragment(R.id.fragmentContainer, EditUsernameFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.menuInflater?.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_edit_name -> {
                replaceFragment(R.id.fragmentContainer, EditNameFragment())
            }
            R.id.settings_menu_logout -> {
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(LoginActivity())
            }
        }
        return true
    }
}