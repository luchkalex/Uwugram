package com.uwugram.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.presentation.components.animationDuration
import com.uwugram.presentation.components.negativeSlideHorizontalOffset
import com.uwugram.presentation.components.positiveSlideHorizontalOffset
import com.uwugram.presentation.screen.*

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun Navigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(navController = navController, startDestination = Screen.LoadingScreen.route) {
        fun appNavGraphComposable(
            route: String,
            arguments: List<NamedNavArgument> = emptyList(),
            content: @Composable (NavBackStackEntry) -> Unit
        ) {
            composable(
                route = route,
                arguments = arguments,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { positiveSlideHorizontalOffset },
                        animationSpec = tween(animationDuration)
                    ) + fadeIn(animationSpec = tween(animationDuration))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { negativeSlideHorizontalOffset },
                        animationSpec = tween(animationDuration)
                    ) + fadeOut(animationSpec = tween(animationDuration))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { negativeSlideHorizontalOffset },
                        animationSpec = tween(animationDuration)
                    ) + fadeIn(animationSpec = tween(animationDuration))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { positiveSlideHorizontalOffset },
                        animationSpec = tween(animationDuration)
                    ) + fadeOut(animationSpec = tween(animationDuration))
                }
            ) { content(it) }
        }

        appNavGraphComposable(route = Screen.LoadingScreen.route) {
            LoadingScreen(
                navController = navController,
            )
        }

        appNavGraphComposable(route = Screen.LoginScreen.route) {
            LoginScreen(
                navController = navController,
            )
        }

        appNavGraphComposable(
            route = Screen.CodeVerificationScreen.route + "/{userID}",
            arguments = listOf(navArgument("userID") { type = NavType.StringType })
        ) { backStackEntry ->
            CodeVerificationScreen(
                navController = navController,
                userID = backStackEntry.arguments?.getString("userID") ?: "",
            )
        }

        appNavGraphComposable(route = Screen.MainScreen.route) {
            ChatsScreen(
                navController = navController,
            )
        }

        appNavGraphComposable(
            route = Screen.SingleChatScreen.route + "/{userID}",
            arguments = listOf(navArgument("userID") { type = NavType.StringType })
        ) { backStackEntry ->
            SingleChatScreen(
                navController = navController,
                userID = backStackEntry.arguments?.getString("userID") ?: "",
            )
        }

        appNavGraphComposable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }

        appNavGraphComposable(route = Screen.EditUsernameScreen.route) {
            EditUsernameScreen(navController = navController)
        }

        appNavGraphComposable(route = Screen.EditBioScreen.route) {
            EditBioScreen(navController = navController)
        }

        appNavGraphComposable(route = Screen.EditNameScreen.route) {
            EditNameScreen(navController = navController)
        }

        appNavGraphComposable(route = Screen.ContactsScreen.route) {
            ContactsScreen(navController = navController)
        }
    }

}