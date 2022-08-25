package com.uwugram.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uwugram.R
import com.uwugram.domain.models.User
import com.uwugram.presentation.screen.Screen
import com.uwugram.theme.ContactImageSize
import com.uwugram.theme.DefaultPadding
import com.uwugram.theme.Dim
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun AppDrawer(
    user: User,
    drawerState: DrawerState,
    navController: NavHostController,
    scope: CoroutineScope,
    content: @Composable () -> Unit,
) {
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(user, navController, drawerState, scope) },
        scrimColor = Dim,
        drawerBackgroundColor = MaterialTheme.colors.background,
        content = content
    )
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun DrawerContent(
    user: User,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Spacer(Modifier.statusBarsHeight())
    Column {
        DrawerHeader(user)
        Spacer(modifier = Modifier.height(DefaultPadding))
        DrawerItem(
            active = false,
            title = stringResource(id = R.string.drawer_item_new_group),
            icon = R.drawable.ic_menu_create_groups,
            contentDescription = stringResource(id = R.string.drawer_item_new_group),
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate(Screen.MainScreen.route)
            }
        )
        DrawerItem(
            active = true,
            title = stringResource(id = R.string.drawer_item_contacts),
            icon = R.drawable.ic_menu_contacts,
            contentDescription = stringResource(id = R.string.drawer_item_contacts),
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate(Screen.ContactsScreen.route)
            }
        )
        DrawerItem(
            active = false,
            title = stringResource(id = R.string.drawer_item_saved_messages),
            icon = R.drawable.ic_menu_favorites,
            contentDescription = stringResource(id = R.string.drawer_item_saved_messages),
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate(Screen.MainScreen.route)
            }
        )
        DrawerItem(
            active = true,
            title = stringResource(id = R.string.drawer_item_settings),
            icon = R.drawable.ic_menu_settings,
            contentDescription = stringResource(id = R.string.drawer_item_settings),
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate(Screen.SettingsScreen.route)
            }
        )
    }
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun DrawerHeader(user: User) {
    val painter = rememberImagePainter(
        data = user.photoURL,
        builder = {
            placeholder(R.drawable.default_avatar)
            error(R.drawable.default_avatar)
            crossfade(1000)
            transformations(
                CircleCropTransformation()
            )
        }
    )
    Column(
        Modifier
            .background(MaterialTheme.colors.surface)
            .padding(DefaultPadding)
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(ContactImageSize)
                .clip(CircleShape),
            painter = painter,
            contentDescription = stringResource(id = R.string.drawer_photo_cd)
        )
        Spacer(modifier = Modifier.height(DefaultPadding))
        Text(
            text = if (user.username.isNotEmpty()) user.username else user.fullName,
            style = MaterialTheme.typography.h1,
        )
        Text(
            text = user.phone,
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
fun DrawerItem(
    active: Boolean,
    title: String,
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.clickable {
        onClick()
    }) {
        Row(
            Modifier
                .padding(DefaultPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                tint = when {
                    active -> MaterialTheme.colors.onBackground
                    else -> MaterialTheme.colors.primaryVariant
                }
            )
            Spacer(modifier = Modifier.padding(horizontal = DefaultPadding))
            Text(
                text = title, style = MaterialTheme.typography.h2, color = when {
                    active -> MaterialTheme.colors.onBackground
                    else -> MaterialTheme.colors.primaryVariant
                }
            )
        }
    }
}