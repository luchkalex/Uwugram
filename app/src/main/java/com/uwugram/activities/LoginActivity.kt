package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.uwugram.R
import com.uwugram.databinding.ActivityLoginBinding
import com.uwugram.utils.replaceFragment
import com.uwugram.view.fragments.EnterPhoneNumberFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        toolbar = binding.loginToolbar
        setSupportActionBar(toolbar)
        title = getString(R.string.login_activity_title)
        replaceFragment(R.id.loginFragmentContainer, EnterPhoneNumberFragment())
    }
}