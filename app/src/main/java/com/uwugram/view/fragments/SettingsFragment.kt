package com.uwugram.view.fragments

import android.view.Menu
import android.view.MenuInflater
import com.uwugram.R

class SettingsFragment : AbstractFragment(R.layout.fragment_settings){

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity?.menuInflater?.inflate(R.menu.settings_menu, menu)
    }
}