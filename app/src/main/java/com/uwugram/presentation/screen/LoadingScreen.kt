package com.uwugram.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.presentation.viewmodel.LoadingViewModel

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun LoadingScreen(
    navController: NavHostController,
    vm: LoadingViewModel = hiltViewModel(),
) {
    vm.checkOnAuthorized(
        onAuthorized = {
            navController.navigate(Screen.MainScreen.route) {
                popUpTo(Screen.LoadingScreen.route) { inclusive = true }
            }
        },
        onNotAuthorized = {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.LoadingScreen.route) { inclusive = true }
            }
        }
    )
}