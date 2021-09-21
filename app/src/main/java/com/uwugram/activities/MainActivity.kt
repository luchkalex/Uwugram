package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.uwugram.R
import com.uwugram.databinding.ActivityMainBinding
import com.uwugram.model.User
import com.uwugram.utils.*
import com.uwugram.view.fragments.ChatFragment
import com.uwugram.view.objects.AppDrawer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toolbar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        initFirebase()
        if (AUTH.currentUser != null) {
            toolbar = binding.mainToolbar
            setSupportActionBar(toolbar)
            initializeUser()
            replaceFragment(R.id.fragmentContainer, ChatFragment(), false)
        } else {
            replaceActivity(LoginActivity())
        }
    }

    private fun initializeUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(USER::class.java) ?: User()
                appDrawer = AppDrawer(this, toolbar)
            })
    }
}