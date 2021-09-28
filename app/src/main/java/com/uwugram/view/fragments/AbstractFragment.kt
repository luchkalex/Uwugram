package com.uwugram.view.fragments

import androidx.fragment.app.Fragment
import com.uwugram.utils.MAIN_ACTIVITY

abstract class AbstractFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        if (MAIN_ACTIVITY.isAppDrawerInitialized()) {
            MAIN_ACTIVITY.appDrawer.disableDrawer()
        }
    }
}