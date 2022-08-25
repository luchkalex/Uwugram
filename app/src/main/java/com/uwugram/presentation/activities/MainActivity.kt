package com.uwugram.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.presentation.navigation.LocalBackPressedDispatcher
import com.uwugram.presentation.navigation.Navigation
import com.uwugram.presentation.viewmodel.MainViewModel
import com.uwugram.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val vm: MainViewModel = hiltViewModel()
            lifecycle.addObserver(vm)
            CompositionLocalProvider(
                LocalBackPressedDispatcher provides onBackPressedDispatcher
            ) {
                AppTheme { Navigation() }
            }
        }
    }
}