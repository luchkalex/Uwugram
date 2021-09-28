package com.uwugram.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import com.uwugram.R
import com.uwugram.databinding.ActivityMainBinding
import com.uwugram.utils.*
import com.uwugram.view.objects.AppDrawer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toolbar: Toolbar
    lateinit var appDrawer: AppDrawer
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        MAIN_ACTIVITY = this
        initFirebase()
        if (AUTH.currentUser != null) {
            toolbar = binding.mainToolbar
            setSupportActionBar(toolbar)
            initializeUser {
                appDrawer = AppDrawer(this, toolbar)
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
                navController = navHostFragment.navController
                if (navController.currentDestination != navController.graph[R.id.chatFragment]) {
                    appDrawer.disableDrawer()
                }
            }
        } else {
            replaceActivity(LoginActivity())
        }
    }

    fun isAppDrawerInitialized(): Boolean {
        return ::appDrawer.isInitialized
    }
}