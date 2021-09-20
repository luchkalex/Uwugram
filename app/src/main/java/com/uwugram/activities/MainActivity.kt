package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.uwugram.R
import com.uwugram.databinding.ActivityMainBinding
import com.uwugram.utils.AUTH
import com.uwugram.utils.initFirebase
import com.uwugram.utils.replaceActivity
import com.uwugram.utils.replaceFragment
import com.uwugram.view.fragments.ChatFragment
import com.uwugram.view.objects.AppDrawer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {
        initFirebase()
        if (AUTH.currentUser != null) {
            toolbar = binding.mainToolbar
            setSupportActionBar(toolbar)
            appDrawer = AppDrawer(this, toolbar)
            replaceFragment(R.id.fragmentContainer, ChatFragment(), false)
        } else {
            replaceActivity(LoginActivity())
        }

    }
}