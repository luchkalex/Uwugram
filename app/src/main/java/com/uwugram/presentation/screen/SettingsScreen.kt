package com.uwugram.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.R
import com.uwugram.domain.models.States
import com.uwugram.domain.models.User
import com.uwugram.presentation.components.DrawerItem
import com.uwugram.presentation.components.ListDivider
import com.uwugram.presentation.components.noRippleClickable
import com.uwugram.presentation.viewmodel.SettingsViewModel
import com.uwugram.theme.ContactImageSize
import com.uwugram.theme.DefaultPadding
import com.uwugram.theme.SmallPadding

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun SettingsScreen(
    navController: NavHostController,
    vm: SettingsViewModel = hiltViewModel()
) {
    val user by remember {
        vm.user
    }

    val selectImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            vm.saveImage(uri)
        })

    SettingsContent(
        user = user,
        onChangePhotoClick = { selectImage.launch("image/*") },
        onChangeUsernameClick = { navController.navigate(Screen.EditUsernameScreen.route) },
        onChangeBioClick = { navController.navigate(Screen.EditBioScreen.route) },
        onChangeNameClick = { navController.navigate(Screen.EditNameScreen.route) },
        onLogoutClick = {
            vm.logout {
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.SettingsScreen.route) { inclusive = true }
                    popUpTo(Screen.MainScreen.route) { inclusive = true }
                }
            }
        }
    )
}

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun SettingsContent(
    user: User,
    onChangePhotoClick: () -> Unit,
    onChangeUsernameClick: () -> Unit,
    onChangeBioClick: () -> Unit,
    onChangeNameClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val menuEnabled = remember {
        mutableStateOf(false)
    }

    val painter = rememberImagePainter(data = user.photoURL,
        builder = {
            placeholder(R.drawable.default_avatar)
            error(R.drawable.default_avatar)
            crossfade(1000)
            transformations(
                CircleCropTransformation()
            )
        })

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = DefaultPadding),
                        text = stringResource(id = R.string.settings_screen_appbar_title),
                        style = MaterialTheme.typography.h1
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                        contentDescription = stringResource(id = R.string.settings_menu_cd),
                        modifier = Modifier
                            .clickable { menuEnabled.value = true }
                            .padding(DefaultPadding)
                    )
                }
            }
        },
        modifier = Modifier.noRippleClickable(
            enabled = menuEnabled.value
        ) { menuEnabled.value = false }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            Column {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .background(MaterialTheme.colors.surface)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(DefaultPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            modifier = Modifier
                                .size(ContactImageSize)
                                .clip(CircleShape),
                            painter = painter,
                            contentDescription = stringResource(id = R.string.settings_photo_cd)
                        )
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = DefaultPadding)
                        ) {
                            Text(user.fullName, style = MaterialTheme.typography.h2)
                            Text(
                                when (user.state) {
                                    States.getStringFromState(States.OFFLINE) -> stringResource(id = R.string.offline_status)
                                    States.getStringFromState(States.ONLINE) -> stringResource(id = R.string.online_status)
                                    else -> ""
                                }, style = MaterialTheme.typography.body2
                            )
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(y = 28.dp)
                            .padding(end = DefaultPadding), onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                            contentDescription = stringResource(id = R.string.settings_change_photo_fab_cd),
                            modifier = Modifier.clickable { onChangePhotoClick() }
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(DefaultPadding),
                    text = stringResource(id = R.string.setting_account_text),
                    style = MaterialTheme.typography.h2
                )
                SettingsItem(
                    text = user.phone,
                    hintText = stringResource(id = R.string.settings_phone_helper_text)
                ) {

                }
                SettingsItem(
                    text = if (user.username.isEmpty()) stringResource(id = R.string.settings_default_username_text) else user.username,
                    hintText = stringResource(id = R.string.settings_username_helper_text),
                    onClick = onChangeUsernameClick
                )
                SettingsItem(
                    text = if (user.bio.isEmpty()) stringResource(id = R.string.settings_default_bio_text) else user.bio,
                    hintText = stringResource(id = R.string.settings_bio_helper_text),
                    onClick = onChangeBioClick
                )
            }

        }
    }

    AnimatedVisibility(
        visible = menuEnabled.value,
        enter = fadeIn(), exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding), contentAlignment = Alignment.TopEnd
        ) {
            SettingsMenu(
                onChangeNameClick = onChangeNameClick,
                onLogoutClick = onLogoutClick
            )

        }
    }

}

@Composable
fun SettingsMenu(onChangeNameClick: () -> Unit, onLogoutClick: () -> Unit) {
    Surface(
        Modifier.clip(RoundedCornerShape(10.dp)), elevation = 10.dp
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colors.background)
                .width(200.dp)
        ) {
            DrawerItem(
                active = true,
                title = stringResource(id = R.string.settings_menu_edit_name),
                icon = R.drawable.ic_baseline_edit_24,
                contentDescription = stringResource(id = R.string.settings_menu_edit_name_cd),
                onClick = onChangeNameClick
            )
            DrawerItem(
                active = true,
                title = stringResource(id = R.string.settings_menu_logout),
                icon = R.drawable.ic_baseline_exit_to_app_24,
                contentDescription = stringResource(id = R.string.settings_menu_logout_cd),
                onClick = onLogoutClick
            )
        }
    }
}

@Composable
fun SettingsItem(text: String, hintText: String, onClick: () -> Unit) {
    Box(Modifier.clickable { onClick() }) {
        Column(Modifier.padding(start = DefaultPadding)) {
            Column(Modifier.padding(vertical = SmallPadding)) {
                Text(text = text, style = MaterialTheme.typography.subtitle1)
                Text(text = hintText, style = MaterialTheme.typography.body2)
            }
            ListDivider()
        }
    }
}

