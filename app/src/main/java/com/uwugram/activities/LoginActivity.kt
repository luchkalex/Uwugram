package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.uwugram.R
import com.uwugram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var toolbar: Toolbar
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        toolbar = binding.loginToolbar
        setSupportActionBar(toolbar)
        title = getString(R.string.login_activity_title)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.loginFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
    }
}