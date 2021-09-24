package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.uwugram.R
import com.uwugram.databinding.ActivityChatBinding
import com.uwugram.utils.*
import com.uwugram.view.fragments.ChatFragment
import com.uwugram.view.objects.AppDrawer

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    lateinit var toolbar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CHAT_ACTIVITY = this
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        initFirebase()
        if (AUTH.currentUser != null) {
            toolbar = binding.chatsToolbar

            setSupportActionBar(toolbar)
            initializeUser {
                appDrawer = AppDrawer(this, toolbar)
                replaceFragment(R.id.chatsFragmentContainer, ChatFragment(), false)
            }
        } else {
            replaceActivity(LoginActivity())
        }
    }
}