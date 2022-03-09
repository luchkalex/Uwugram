package com.uwugram.presentation.screen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object LoginScreen : Screen("login_screen")
    object CodeVerificationScreen : Screen("code_verification_screen")
    object LoadingScreen : Screen("loading_screen")
    object SingleChatScreen : Screen("single_chat_screen")
    object SettingsScreen : Screen("settings_screen")
    object ContactsScreen : Screen("contacts_screen")
    object EditUsernameScreen : Screen("edit_username_screen")
    object EditBioScreen : Screen("edit_bio_screen")
    object EditNameScreen : Screen("edit_name_screen")
}
