package com.uwugram.view.fragments

import androidx.fragment.app.Fragment
import com.uwugram.activities.MainActivity

abstract class AbstractFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).appDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).appDrawer.enableDrawer()
    }
}