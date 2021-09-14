package com.uwugram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.uwugram.databinding.ActivityMainBinding
import com.uwugram.view.objects.AppDrawer
import com.uwugram.view.fragments.ChatFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var appDrawer: AppDrawer


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
        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
        appDrawer = AppDrawer(this, toolbar)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ChatFragment()).commit()
    }
}