package com.uwugram.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.activities.LoginActivity
import com.uwugram.activities.MainActivity
import com.uwugram.databinding.FragmentSettingsBinding
import com.uwugram.utils.AUTH
import com.uwugram.utils.replaceActivity

class SettingsFragment : Fragment() {

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

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.menuInflater?.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_logout -> {
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(LoginActivity())
            }
        }
        return true
    }
}