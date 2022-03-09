package com.uwugram.presentation.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.permissions.*
import com.uwugram.R
import com.uwugram.domain.models.User
import com.uwugram.presentation.components.AppDrawer
import com.uwugram.presentation.components.AppTopBar
import com.uwugram.presentation.components.ContactItem
import com.uwugram.presentation.components.ListDivider
import com.uwugram.presentation.navigation.BackPressHandler
import com.uwugram.presentation.viewmodel.ChatsViewModel
import com.uwugram.theme.DefaultPadding
import com.uwugram.theme.SmallPadding
import com.uwugram.utils.READ_CONTACTS
import com.uwugram.utils.getActivity
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun ChatsScreen(
    navController: NavHostController,
    vm: ChatsViewModel = hiltViewModel(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerOpen by vm.drawerShouldBeOpened.collectAsState()
    val scope = rememberCoroutineScope()


    val user by remember { vm.user }
    val showNoChatsMessage by remember { vm.showNoChatsMessage }

    val permissionState = rememberPermissionState(permission = READ_CONTACTS)
    val showRationale = remember {
        mutableStateOf(true)
    }

    if (drawerOpen) {
        LaunchedEffect(Unit) {
            drawerState.open()
            vm.resetOpenDrawerAction()
        }
    }

    if (drawerState.isOpen) {
        BackPressHandler {
            scope.launch {
                drawerState.close()
            }
        }
    }

    val activity = LocalContext.current.getActivity()

    AppDrawer(
        user = user,
        drawerState = drawerState,
        navController = navController,
        scope = scope
    ) {
        if (permissionState.status.isGranted || !showRationale.value || !vm.showRequestPermission) {
            if (permissionState.status.isGranted) {
                activity?.let { vm.initializeContacts(it) }
            }
            ChatsContent(
                chatsList = vm.chatList,
                onDrawerIconClick = {
                    vm.openDrawer()
                },
                showNoChatsMessage = showNoChatsMessage,
                navController = navController,
            )
        } else if (!(permissionState.status as PermissionStatus.Denied).shouldShowRationale ||
            showRationale.value
        ) {
            ContactPermissionContent(
                permissionState = permissionState,
                onCancelClick = {
                    showRationale.value = false
                    vm.closePermissionScreen()
                }
            )
        }
    }
}

@Composable
@ExperimentalPermissionsApi
private fun ContactPermissionContent(
    permissionState: PermissionState,
    onCancelClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(DefaultPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.contacts_access_rationale),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(DefaultPadding))
        Button(onClick = {
            permissionState.launchPermissionRequest()
            onCancelClick()
        }) {
            Text(text = stringResource(id = R.string.contacts_access_button_text))
        }
    }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
        Icon(
            modifier = Modifier
                .clickable { onCancelClick() }
                .padding(SmallPadding),
            painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = stringResource(id = R.string.contacts_access_cancel_button_cd)
        )
    }
}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
private fun ChatsContent(
    chatsList: List<User>,
    onDrawerIconClick: () -> Unit,
    navController: NavController,
    showNoChatsMessage: Boolean
) {
    Scaffold(
        topBar = {
            AppTopBar {
                Icon(
                    painter = painterResource(id = R.drawable.open_drawer_icon),
                    contentDescription = stringResource(id = R.string.open_drawer_button_cd),
                    modifier = Modifier.clickable { onDrawerIconClick() }
                )
                Spacer(modifier = Modifier.width(DefaultPadding))
                Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.h1)
            }
        }
    ) {
        if (showNoChatsMessage) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.no_conversations_message),
                    style = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center)
                )
            }
        } else {
            LazyColumn {
                items(chatsList) {
                    ContactItem(
                        image = it.photoURL,
                        title = it.fullName,
                        subtitle = it.message.text
                    ) {
                        navController.navigate(Screen.SingleChatScreen.route + "/" + it.id)
                    }
                    Box(modifier = Modifier.padding(start = DefaultPadding)) { ListDivider() }
                }
            }
        }
    }
}
