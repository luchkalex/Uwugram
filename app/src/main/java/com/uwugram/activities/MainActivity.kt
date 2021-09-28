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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
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
                CoroutineScope(Dispatchers.IO).launch {
                    initContacts()
                }

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionCodes.PERMISSION_READ_CONTACTS_REQUEST_CODE.code -> {
                if (checkPermission(READ_CONTACTS)) initContacts()
                else showShortToast(getString(R.string.permission_denied_message))
            }
        }
    }

    fun isAppDrawerInitialized(): Boolean {
        return ::appDrawer.isInitialized
    }
}