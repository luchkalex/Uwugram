package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.databinding.ActivityMainBinding
import com.uwugram.utils.Signals
import com.uwugram.utils.replaceActivity
import com.uwugram.utils.replaceFragment
import com.uwugram.utils.updateUserState
import com.uwugram.view.fragments.SettingsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar

    companion object {
        var fragment: Fragment = SettingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        replaceFragment(R.id.fragmentContainer, fragment, false)
    }

    override fun onStart() {
        super.onStart()
        updateUserState(Signals.START)
    }

    override fun onStop() {
        super.onStop()
        updateUserState(Signals.STOP)
    }

    private fun initialize() {
        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            replaceActivity(ChatActivity()).also { updateUserState(Signals.REPLACE) }
        }
    }
}