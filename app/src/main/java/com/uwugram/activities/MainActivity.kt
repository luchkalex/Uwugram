package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.uwugram.R
import com.uwugram.databinding.ActivityMainBinding
import com.uwugram.utils.replaceActivity
import com.uwugram.utils.replaceFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar

    companion object {
        lateinit var fragment: Fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        replaceFragment(R.id.fragmentContainer, fragment, false)
    }

    private fun initialize() {
        toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            replaceActivity(ChatActivity())
        }
    }
}